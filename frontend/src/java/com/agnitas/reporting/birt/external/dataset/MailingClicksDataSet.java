/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.reporting.birt.external.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.agnitas.reporting.birt.external.beans.ClickingRecipientsRow;
import com.agnitas.reporting.birt.external.utils.FormatTools;

/**
 * BIRT-DataSet for displaying 'Clicking-Recipients' chart and values
 */
public class MailingClicksDataSet extends BIRTDataSet {
	/** The logger. */
	private static final transient Logger logger = Logger.getLogger(MailingClicksDataSet.class);
	
	public List<ClickingRecipientsRow> getClickingRecipients(String mailingID , String companyID , Boolean includeAdminAndTestMails) {
		// TODO: Company ID is not checkable. Change to "int"
		List<ClickingRecipientsRow> list = new ArrayList<>();
		String query = "SELECT 'clicking recipients' recipients, COUNT(DISTINCT customer_id) clicking_recipients"
			+ " FROM rdirlog_" + companyID + "_tbl"
			+ " WHERE mailing_id = " + mailingID;

		int totalSent =  new MailingSendDataSet().getTotalSend(Integer.parseInt(mailingID), includeAdminAndTestMails);
		List<Map<String, Object>> result = select(logger, query);
		if (result.size() > 0) {
			ClickingRecipientsRow tmp = new ClickingRecipientsRow();
			tmp.setCategory((String) result.get(0).get("recipients"));
			tmp.setClicking_recpients(((Number) result.get(0).get("clicking_recipients")).intValue());
			if ( totalSent != 0 ) {
				tmp.setClicking_recipients_percent( (float) FormatTools.roundDecimal(tmp.getClicking_recpients() * 1.0f / totalSent, 1));
			}
			list.add( tmp );
		}
		return list;
	}
}
