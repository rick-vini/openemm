/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.emm.core.reminder.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.agnitas.dao.impl.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.agnitas.emm.core.reminder.dao.ComReminderDao;

public abstract class ComReminderBaseDaoImpl extends BaseDaoImpl implements ComReminderDao {
	private static final transient Logger logger = Logger.getLogger(ComReminderBaseDaoImpl.class);
	private final SenderNameRowMapper senderNameRowMapper = new SenderNameRowMapper();

	public String getSenderName(int adminId) {
		if (adminId > 0) {
			String query = "SELECT fullname, company_name FROM admin_tbl WHERE admin_id = ?";
			String name = selectObjectDefaultNull(logger, query, senderNameRowMapper, adminId);

			return StringUtils.defaultString(name);
		}

		return "";
	}

	protected String getSenderName(String adminName, String companyName) {
		if (StringUtils.isNotEmpty(companyName)) {
			if (StringUtils.isNotEmpty(adminName)) {
				return companyName + "/" + adminName;
			} else {
				return companyName;
			}
		}

		if (StringUtils.isNotEmpty(adminName)) {
			return adminName;
		} else {
			return "";
		}
	}

	private class SenderNameRowMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int i) throws SQLException {
			String company = rs.getString("company_name");
			String name = rs.getString("fullname");

			return getSenderName(name, company);
		}
	}
}
