/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.dao;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.agnitas.beans.Campaign;
import org.agnitas.beans.MailingBase;
import org.agnitas.emm.core.velocity.VelocityCheck;

import com.agnitas.beans.ComCampaign;
import com.agnitas.beans.ComCampaignStats;

public interface ComCampaignDao {

	ComCampaignStats getStats(boolean useMailtracking, Locale aLocale, List<Integer> mailingIDs, ComCampaign campaign, String mailingSelection, int targetID, ComTargetDao targetDao, ComRevenueDao revenueDao);
	ComCampaign getCampaign(int campaignID, @VelocityCheck int companyID);
	List<Map<String, Object>> getMailingNames(ComCampaign campaign, String mailingSelection);
	int deleteByCompanyID(@VelocityCheck int companyID);
	 /**
     *  Saves campaign data
     *
     * @param campaign
     * @return the ID of the saved campaign
     */
    int save(Campaign campaign);
	/**
     * Deletes campaign from database
     *
     * @param campaign
     * @return true if the Campaign has been deleted
     */
	boolean delete(Campaign campaign);
	/**
     * Loads list of sent mailings which are related to the certain archive
     *
     * @param campaignID
     *                The id of the campaign
     * @param companyID
     *                The id of the campaign company
     * @return  List of MailingBase bean objects or empty list
     */
    List<MailingBase> getCampaignMailings(int campaignID, @VelocityCheck int companyID);

    /**
     * Loads list of campaigns for certain company; sort and order criteria are used for getting sorted selection from database
     *
     * @param companyID
     *              The id of the company of campaigns
     * @param sort
     *              The name of column for sorting
     * @param order
     *              The sort order , 1 (for ascending) or 2 (for descending)
     *
     * @return List of Campaign bean objects or empty list
     */
    List<Campaign> getCampaignList( @VelocityCheck int companyID, String sort, int order);

}
