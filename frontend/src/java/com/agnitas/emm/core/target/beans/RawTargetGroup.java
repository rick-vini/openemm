/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.emm.core.target.beans;

import org.agnitas.target.TargetRepresentation;

/**
 * The class represents raw target group data.
 * 
 * These information is read from database and not processed furthermore.
 * This is used when EQL expression and {@link TargetRepresentation} are required in
 * an unmodified form.
 * 
 * Therefore
 * <ul>
 *   <li>EQL may be invalid (due to missing profile fields or reference tables)</li>
 *   <li>EQL and {@link TargetRepresentation} may not represent the same target group rules</li>
 * </ul>
 */
public final class RawTargetGroup {

	/** ID of the target group. */
	private final int id;
	
	/** Name of the target group. */
	private final String name;
	
	private final int companyId;
	
	/** EQL expression of the target group. */
	private final String eql;
	
	/** {@link TargetRepresentation} of the target group. */
	private final TargetRepresentation representation;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param id ID of target group
	 * @param name name of target group
	 * @param eql EQL expression
	 * @param representation 
	 */
	public RawTargetGroup(final int id, final String name, int companyId, final String eql, final TargetRepresentation representation) {
		this.id = id;
		this.name = name;
		this.companyId = companyId;
		this.eql = eql;
		this.representation = representation;
	}

	/**
	 * Returns the target group ID.
	 * 
	 * @return the target group ID
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Returns the name of the target group.
	 * 
	 * @return name of the target group
	 */
	public final String getName() {
		return name;
	}
	
	public int getCompanyId() {
		return companyId;
	}
	
	/**
	 * Returns the (unmodified) EQL expression of the target group.
	 * 
	 * @return the (unmodified) EQL expression of the target group
	 */
	public final String getEql() {
		return eql;
	}

	/**
	 * Returns the (unmodified) {@link TargetRepresentation} of the target group.
	 * 
	 * @return the (unmodified) {@link TargetRepresentation} of the target group
	 */
	public final TargetRepresentation getRepresentation() {
		return representation;
	}
	 
}
