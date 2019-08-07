/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package org.agnitas.dao.impl;

import java.util.List;
import java.util.Map;

import com.agnitas.emm.core.mobile.bean.DeviceClass;
import com.agnitas.emm.core.mobile.service.ComDeviceService;
import org.agnitas.beans.BindingEntry;
import org.agnitas.dao.OnepixelDao;
import org.agnitas.emm.core.commons.util.ConfigService;
import org.agnitas.emm.core.commons.util.ConfigValue;
import org.agnitas.emm.core.velocity.VelocityCheck;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import com.agnitas.dao.DaoUpdateReturnValueCheck;

public class OnepixelDaoImpl extends BaseDaoImpl implements OnepixelDao {
	
	private static final transient Logger logger = Logger.getLogger( OnepixelDaoImpl.class);
	
	private static final String FIELD_OPEN_COUNT = "open_count";
	private static final String FIELD_MOBILE_COUNT = "mobile_count";

	public static String getOnepixellogDeviceTableName(@VelocityCheck int companyId) {
		return new StringBuilder("onepixellog_device_").append(companyId).append("_tbl").toString();
	}
	
	public static String getOnepixellogTableName(@VelocityCheck int companyId) {
		return new StringBuilder("onepixellog_").append(companyId).append("_tbl").toString();
	}
	
	private static String getSqlInsertString(@VelocityCheck int companyId) {
		return "INSERT INTO " + getOnepixellogTableName(companyId) + " (company_id, mailing_id, customer_id, ip_adr, open_count, mobile_count, timestamp, first_open, last_open) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
	}
	
	private static String getSqlUpdateString_Oracle(@VelocityCheck int companyId) {
		return "UPDATE " + getOnepixellogTableName(companyId) + " SET open_count = open_count + 1, mobile_count = NVL(mobile_count, 0) + ?, last_open = CURRENT_TIMESTAMP WHERE company_id = ? AND mailing_id = ? AND customer_id = ?";
	}

	private static String getSqlUpdateString_MySql(@VelocityCheck int companyId) {
		return "UPDATE " + getOnepixellogTableName(companyId) + " SET open_count = open_count + 1, mobile_count = IFNULL(mobile_count, 0) + ?, last_open = CURRENT_TIMESTAMP WHERE company_id = ? AND mailing_id = ? AND customer_id = ?";
	}
	
	private static String getSqlDeviceInsertString(int companyId) {
		return "INSERT INTO " + getOnepixellogDeviceTableName(companyId) + " (company_id, mailing_id, customer_id, device_class_id, device_id, client_id, creation) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
	}

    private ConfigService configService;
	
	@Required
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	private void updateCustomerForOpen(int companyID, int customerID) {
		if(customerID != 0) {
			try {
				String updateLastOpenStatement = "UPDATE customer_" + companyID + "_tbl SET lastopen_date = CURRENT_TIMESTAMP WHERE customer_id = ?";
				update(logger, updateLastOpenStatement, customerID);
			} catch (Exception e) {
				if(logger.isInfoEnabled()) {
					logger.info(String.format("Cannot update last open date for customer %d of company %d", customerID, companyID), e);
				}
			}
		}
	}
	
	@Override
	@DaoUpdateReturnValueCheck
    public boolean writePixel(@VelocityCheck int companyID, int recipientID, int mailingID, String remoteAddr, DeviceClass deviceClass, int deviceID, int clientID) {
        try {
        	int mobileCountDelta = 0;
        	if (deviceClass == DeviceClass.MOBILE) {
        		mobileCountDelta = 1;
        	}
        	
    		int touchedLines;
    		if (isOracleDB()) {
    			touchedLines = update(logger, getSqlUpdateString_Oracle(companyID), mobileCountDelta, companyID, mailingID, recipientID);
    		} else {
    			touchedLines = update(logger, getSqlUpdateString_MySql(companyID), mobileCountDelta, companyID, mailingID, recipientID);
    		}
    		
        	if (touchedLines == 0) {
				// Insert new entry
        		update(logger, getSqlInsertString(companyID), companyID, mailingID, recipientID, remoteAddr, 1, mobileCountDelta);
			} else if (touchedLines > 1) {
				// If more than 1 row was incremented, then we have to subtract the number of wrongly incremented rows
				final int correctionValue = touchedLines - 1;
				
				logger.error("Invalid data: invalid number of entries found for companyid:" + companyID + ", mailingid:" + mailingID + ", custid:" + recipientID + " , entries:" + touchedLines + " (cleaning up entries exept for the first one)");
				// If more than one entry exists, there was an error on the first insert caused by parallel inserts, which will be repaired now
				// We delete all entries and insert a new single one with the sum of counting of the deleted ones
				// This is a bit of optimistic, because another process could have found this problem too, and tries to resolve it. But this will not happen too many times
				List<Map<String, Object>> result = select(logger, "SELECT " + FIELD_OPEN_COUNT + ", " + FIELD_MOBILE_COUNT + " FROM " + getOnepixellogTableName(companyID) + " WHERE mailing_id = ? AND customer_id = ?", mailingID, recipientID);
				if (result.size() > 1) {
					// Sum up existing entries (not using sum(...) for checking there is still more than 1 entry)
					int countSum = 0;
					int mobileSum = 0;
					for (Map<String, Object> row : result) {
						countSum += ((Number) row.get(FIELD_OPEN_COUNT)).intValue();
						mobileSum += ((Number) row.get(FIELD_MOBILE_COUNT)).intValue();
					}
					// Delete existing entries
					update(logger, "DELETE FROM " + getOnepixellogTableName(companyID) + " WHERE mailing_id = ? AND customer_id = ?", mailingID, recipientID);
					// Insert summed up entry
					update(logger, getSqlInsertString(companyID), companyID, mailingID, recipientID, remoteAddr, countSum - correctionValue, mobileSum - correctionValue);
				}
			}
        	
        	// write additional device entry
			update(logger, getSqlDeviceInsertString(companyID), companyID, mailingID, recipientID, deviceClass.getId(), deviceID, clientID);
			
			// Update customer entry
			if (configService.getBooleanValue(ConfigValue.WriteCustomerOpenOrClickField, companyID)) {
				updateCustomerForOpen(companyID, recipientID);
			}
			
			return true;
        } catch (Exception e) {
            logger.error("Error writing OnePixelLog writePixel", e);
            return false;
        }
    }
	
	@Override
	@DaoUpdateReturnValueCheck
	public boolean writePixel(@VelocityCheck int companyID, int recipientID, int mailingID, String remoteAddr)	{
		JdbcTemplate jdbc = new JdbcTemplate(getDataSource());
		String sql = null;

		try {
			sql="update onepixel_log_tbl set open_count = open_count+1 where company_id = ? and customer_id = ?  and mailing_id = ?";
                                

			if(jdbc.update(sql, new Object[] { new Integer(companyID), new Integer(recipientID), new Integer(mailingID)}) > 0) {
				return true;
			}
			sql="insert into onepixel_log_tbl (company_id, customer_id, mailing_id, open_count, ip_adr, timestamp) VALUES (?, ?, ?, 1, ?, CURRENT_TIMESTAMP)";
			if(jdbc.update(sql, new Object[] {new Integer(companyID), new Integer(recipientID), new Integer(mailingID), remoteAddr}) > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error( "sql: " + sql, e);
			javaMailService.sendExceptionMail("sql:" + sql, e);
		}
		return false;
	}
	
	@Override
	@DaoUpdateReturnValueCheck
    public void deleteAdminAndTestOpenings(int mailingId, @VelocityCheck int companyId) {
        // remove from onepixellog_X_tbl
        String query = createDeleteAdminAndTestOpeningsQuery(getOnepixellogTableName(companyId), companyId);
        update(logger, query, mailingId, mailingId);
        // remove from onepixellog_device_X_tbl
        query = createDeleteAdminAndTestOpeningsQuery(getOnepixellogDeviceTableName(companyId), companyId);
        update(logger, query, mailingId, mailingId);
    }

    private String createDeleteAdminAndTestOpeningsQuery(String tableName, @VelocityCheck int companyId) {
        String deleteAlias = "";
        if (!isOracleDB()) {
            deleteAlias = "lg";
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE ");
        queryBuilder.append(deleteAlias);
        queryBuilder.append(" FROM ");
        queryBuilder.append(tableName);
        queryBuilder.append(" lg WHERE lg.mailing_id = ? AND EXISTS (SELECT 1 FROM customer_");
        queryBuilder.append(companyId);
        queryBuilder.append("_binding_tbl bind WHERE bind.user_type IN ('" + BindingEntry.UserType.Admin.getTypeCode() + "', '" + BindingEntry.UserType.TestUser.getTypeCode() + "', '" + BindingEntry.UserType.TestVIP.getTypeCode() + "') ");
        queryBuilder.append("AND lg.customer_id = bind.customer_id AND bind.mailinglist_id = (SELECT mailinglist_id FROM mailing_tbl WHERE mailing_id = ?))");
        return queryBuilder.toString();
    }
}
