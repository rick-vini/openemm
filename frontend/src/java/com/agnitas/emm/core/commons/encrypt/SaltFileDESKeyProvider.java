/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.emm.core.commons.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Implementation of {@link KeyProvider} to read DES-keys from salt files
 */
public class SaltFileDESKeyProvider implements KeyProvider {

	/** Key created from salt file. */
	private final byte[] key;
	
	/**
	 * Creates the key provider for given salt file.
	 * 
	 * @param file salt file
	 * 
	 * @throws IOException on errors reading key
	 */
	public SaltFileDESKeyProvider(File file) throws IOException {
		this.key = readKey(file);
	}
	
	/**
	 * Read key from salt file.
	 * 
	 * @param file salt file
	 * 
	 * @return key from salt file
	 * 
	 * @throws IOException on errors reading key
	 */
	private static byte[] readKey(File file) throws IOException {
		byte[] keyBuffer = new byte[8];
		
		try(FileInputStream stream = new FileInputStream(file)) {
			int totalRead = 0;
			int read = 0;
			
			while(totalRead < keyBuffer.length && (read = stream.read(keyBuffer, totalRead, keyBuffer.length - totalRead)) != -1) {
				totalRead += read;
			}
			
			fillBuffer(keyBuffer, totalRead);
		}

		return keyBuffer;
	}
	
	/**
	 * Fills buffer if not enough bytes where read from salt file.
	 * 
	 * @param buffer buffer containing bytes from file
	 * @param validBytes number of bytes read from file
	 * 
	 */
	private static void fillBuffer(byte[] buffer, int validBytes) {
		// Copy bytes from start to unfilled buffer
		for(int i = validBytes; i < buffer.length; i++) {
			buffer[i] = buffer[i - validBytes] ;
		}
	}
	
	@Override
	public byte[] getEncryptionKey() {
		return this.key;
	}

}
