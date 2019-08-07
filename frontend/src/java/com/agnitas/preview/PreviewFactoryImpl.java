/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.preview;

import org.agnitas.preview.Preview;
import org.agnitas.preview.PreviewFactory;
import org.agnitas.preview.PreviewImpl;

public class PreviewFactoryImpl implements PreviewFactory {

	private static PreviewFactory instance;
	private Preview preview;
	
	private PreviewFactoryImpl() {
		
	}

	public static PreviewFactory createInstance() {
		if ( instance == null ) {
			instance = new PreviewFactoryImpl();
		}
		return instance;
	}
	
	@Override
	public Preview createPreview() {
		if( preview == null) {
			preview = new PreviewImpl();
		}
		return preview;
	}

}
