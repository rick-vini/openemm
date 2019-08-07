/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.beans;

import java.util.Map;

import org.agnitas.beans.CampaignStats;

public interface ComCampaignStats extends CampaignStats {
	 
	public Map<Integer, Double> getRevenues();	// return the revenues from a Campaign.
	public void setRevenues(Map<Integer, Double> revenues);	// sets the revenues.
	public double getBiggestRevenue();	// get the biggest Revenue of this campaign
	public void setBiggestRevenue(double in_Revenue);// set the biggest revenue of this campaign
	public double getTotalRevenue();	// get the sum of all Revenues of this campaign
	public void setTotalRevenue(double in_Revenue);	// sets the sum of all revenues of this campaign.	
}
