/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.emm.core.loginmanager.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.agnitas.emm.core.loginmanager.dto.BlockedAddressDto;
import com.agnitas.emm.core.loginmanager.form.BlockedAddressForm;

@Component
public class BlockedAddressDtoToBlockedAddressFormConverter implements Converter<BlockedAddressDto, BlockedAddressForm> {
	
	@Override
	public BlockedAddressForm convert(BlockedAddressDto source) {
		BlockedAddressForm form = new BlockedAddressForm();
		form.setTrackingId(source.getTrackingId());
		form.setUsername(source.getUsername());
		form.setIpAddress(source.getIpAddress());
		return form;
	}
}
