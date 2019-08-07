/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package org.agnitas.emm.extension.data.impl;

import java.util.Collection;
import java.util.Vector;

import org.agnitas.emm.extension.data.ExtensionStatus;
import org.agnitas.emm.extension.data.PluginDetail;

public class PluginDetailImpl extends PluginStatusImpl implements PluginDetail {

	private Collection<ExtensionStatus> extensionStatusList = new Vector<>();
	private Collection<String> dependingPluginIds = new Vector<>();
	private boolean systemPlugin;

	@Override
	public Collection<ExtensionStatus> getExtensionStatusList() {
		return this.extensionStatusList;
	}

	public void setExtensionStatusList( Collection<ExtensionStatus> extensionStatusList) {
		this.extensionStatusList = extensionStatusList;
	}

	@Override
	public Collection<String> getDependingPluginIds() {
		return this.dependingPluginIds;
	}
	
	public void setDependingPluginIds(Collection<String> dependingPluginIds) {
		this.dependingPluginIds = dependingPluginIds;
	}

	@Override
	public boolean isSystemPlugin() {
		return this.systemPlugin;
	}
	
	public void setSystemPlugin(boolean systemPlugin) {
		this.systemPlugin = systemPlugin;
	}
}
