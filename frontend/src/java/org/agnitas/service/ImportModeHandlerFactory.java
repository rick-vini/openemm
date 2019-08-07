/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package org.agnitas.service;

import org.agnitas.util.importvalues.ImportModeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ImportModeHandlerFactory implements ApplicationContextAware {
	protected ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
	    this.applicationContext = applicationContext;
	}

	public ImportModeHandler getImportModeHandler(String importModeHandlerName) {
		return applicationContext.getBean(importModeHandlerName, ImportModeHandler.class);
	}
}
