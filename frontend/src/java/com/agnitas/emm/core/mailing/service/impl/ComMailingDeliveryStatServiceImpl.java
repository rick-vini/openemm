/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.emm.core.mailing.service.impl;

import java.util.Date;

import org.agnitas.beans.Mailing;
import org.agnitas.dao.MailingDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.agnitas.beans.DeliveryStat;
import com.agnitas.beans.MaildropEntry;
import com.agnitas.beans.impl.MailingBackendLog;
import com.agnitas.dao.DeliveryStatDao;
import com.agnitas.emm.core.mailing.service.ComMailingDeliveryStatService;

public class ComMailingDeliveryStatServiceImpl implements ComMailingDeliveryStatService {

    private static final Logger logger = Logger.getLogger(ComMailingDeliveryStatServiceImpl.class);

    private DeliveryStatDao deliveryStatDao;
    private MailingDao mailingDao;

    @Override
    public DeliveryStat getDeliveryStats(int companyID, int mailingID, int mailingType) {
        DeliveryStat deliveryStatistic = new DeliveryStat();
        deliveryStatistic.setTotalMails(deliveryStatDao.getTotalMails(mailingID));

        int statusID = 0;
        // -------------------------------------- last thing backend did for this mailing:
        try {
            MaildropEntry maildropStatus = deliveryStatDao.getLastMaildropStatus(mailingID);
            if (maildropStatus != null) {
                if (maildropStatus.getGenStatus() > 0) {
                    deliveryStatistic.setLastType(String.valueOf(maildropStatus.getStatus()));
                } else {
                    deliveryStatistic.setCancelable(true);
                    deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_SCHEDULED);
                }
                statusID = maildropStatus.getId();
            }
        } catch (Exception e) {
            return null;
        }

        // ----------------------------------------- how many mails in last admin/test backend action?
        boolean lastBackend;
        try {
            MailingBackendLog mailingBackendLog = deliveryStatDao.getLastMailingBackendLog(statusID);
            if (mailingBackendLog != null) {
                deliveryStatistic.setLastGenerated(mailingBackendLog.getCurrentMails());
                deliveryStatistic.setLastTotal(mailingBackendLog.getTotalMails());
                deliveryStatistic.setLastDate(mailingBackendLog.getTimestamp());
                deliveryStatistic.setGenerateStartTime(mailingBackendLog.getCreationDate());
            } else {
                deliveryStatistic.setLastDate(new Date());
            }
            lastBackend = true;
        } catch (Exception e) {
            lastBackend = false;
        }

        // no entry in mailing_backend_log_tbl ==> don't proceed:
        if (!"NO".equals(deliveryStatistic.getLastType()) && !lastBackend) {
            return deliveryStatistic;
        }

        String statusField;
        switch (mailingType) {
            case Mailing.TYPE_NORMAL:
                statusField = "W";
                break;

            case Mailing.TYPE_DATEBASED:
                statusField = "R";
                break;

            case Mailing.TYPE_ACTIONBASED:
                statusField = "C";
                break;
                
			default:
				statusField = "W";
        }

        // check generation status first
        try {
            MaildropEntry maildropGenerationStatus = deliveryStatDao.getFirstMaildropGenerationStatus(companyID, mailingID, statusField);
            if (maildropGenerationStatus != null) {
                deliveryStatistic.setOptimizeMailGeneration(maildropGenerationStatus.getMailGenerationOptimization());
                deliveryStatistic.setScheduledGenerateTime(maildropGenerationStatus.getGenDate());
                deliveryStatistic.setScheduledSendTime(maildropGenerationStatus.getSendDate());
                int aktMdropStatus = maildropGenerationStatus.getGenStatus();

                switch (aktMdropStatus) {
                    case MaildropEntry.GEN_SCHEDULED:
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_SCHEDULED);
                        deliveryStatistic.setCancelable(true);
                        break;
                    case MaildropEntry.GEN_NOW:
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_SCHEDULED);
                        break;
                    case MaildropEntry.GEN_WORKING:
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_GENERATING);
                        break;
                    case MaildropEntry.GEN_FINISHED:
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_GENERATED);
                        break;
                    default:
                    	throw new Exception("Invalid gen status type");
                }
            } else {
                // mailing not scheduled for sending:
                deliveryStatistic.setCancelable(false);
                deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_NOT_SENT);
            }
        } catch (Exception e) {
            return deliveryStatistic;
        }

        //----------------------------------------------------------------------
        if (!deliveryStatistic.getLastType().equalsIgnoreCase("W")) {
            int lastWorldMailingStatusID = mailingDao.getStatusidForWorldMailing(deliveryStatistic.getMailingID(), deliveryStatistic.getCompanyID());
            if (lastWorldMailingStatusID != 0) {
                // mailing has been sent, but a test or admin mailing follows
                MailingBackendLog lastWorldMailingBackendLog = deliveryStatDao.getLastMailingBackendLog(lastWorldMailingStatusID);

                deliveryStatistic.setTotalMails(lastWorldMailingBackendLog.getTotalMails());
                deliveryStatistic.setGeneratedMails(lastWorldMailingBackendLog.getCurrentMails());
                deliveryStatistic.setGenerateEndTime(lastWorldMailingBackendLog.getTimestamp());
                deliveryStatistic.setGenerateStartTime(lastWorldMailingBackendLog.getCreationDate());
                deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_SENT);

                deliveryStatistic.setSentMails(deliveryStatDao.getSentMails(lastWorldMailingStatusID));
                deliveryStatistic.setSendStartTime(deliveryStatDao.getSendStartTime(lastWorldMailingStatusID));
                deliveryStatistic.setSendEndTime(deliveryStatDao.getSendEndTime(lastWorldMailingStatusID));
            }
        } else {
            // detailed stats for mailings beeing generated:
            try {
                MailingBackendLog mailingBackendLog = deliveryStatDao.getLastMailingBackendLog(statusID);
                if (mailingBackendLog != null) {
                    deliveryStatistic.setTotalMails(mailingBackendLog.getTotalMails());
                    deliveryStatistic.setGeneratedMails(mailingBackendLog.getCurrentMails());
                    deliveryStatistic.setGenerateEndTime(mailingBackendLog.getTimestamp());
                    deliveryStatistic.setGenerateStartTime(mailingBackendLog.getCreationDate());

                    // loadSendDate
                    try {
                        deliveryStatistic.setScheduledSendTime(deliveryStatDao.getSendDateFoStatus(statusID));
                    } catch (Exception e) {
                        return deliveryStatistic;
                    }

                    deliveryStatistic.setSentMails(deliveryStatDao.getSentMails(statusID));
                    deliveryStatistic.setSendStartTime(deliveryStatDao.getSendStartTime(statusID));
                    deliveryStatistic.setSendEndTime(deliveryStatDao.getSendEndTime(statusID));

                    if (deliveryStatistic.getGeneratedMails() == deliveryStatistic.getTotalMails()
                            && deliveryStatistic.getSentMails() == deliveryStatistic.getTotalMails()) {
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_SENT);
                    } else if (deliveryStatistic.getGeneratedMails() == deliveryStatistic.getTotalMails()
                            && deliveryStatistic.getScheduledSendTime().after(new Date())) {
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_GENERATED);
                    } else if (deliveryStatistic.getGeneratedMails() == deliveryStatistic.getTotalMails()) {
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_SENDING);
                    } else {
                        deliveryStatistic.setDeliveryStatus(DeliveryStat.STATUS_GENERATING);
                    }
                }
            } catch (Exception e) {
                logger.error("Error in getDeliveryStatsForMailingType(" + companyID + ", " + mailingID + ", " + mailingType + "): " + e.getMessage(), e);
                return deliveryStatistic;
            }

            // cancel only mailings the generation has not yet begun at the
            // moment:
            if (deliveryStatistic.getDeliveryStatus() == DeliveryStat.STATUS_SCHEDULED) {
                deliveryStatistic.setCancelable(true);
            } else {
                deliveryStatistic.setCancelable(false);
            }
        }

        return deliveryStatistic;
    }

    @Override
    public boolean cancelDelivery(int companyID, int mailingID) {
        return deliveryStatDao.cancelDelivery(companyID, mailingID);
    }

    @Required
    public void setDeliveryStatDao(DeliveryStatDao deliveryStatDao) {
        this.deliveryStatDao = deliveryStatDao;
    }

    @Required
    public void setMailingDao(MailingDao mailingDao) {
        this.mailingDao = mailingDao;
    }
}
