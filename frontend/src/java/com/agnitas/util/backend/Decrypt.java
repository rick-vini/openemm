/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.util.backend;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * This class handles encrypted data that had been generate by
 * the backend. As secretKey use company_tbl.secret_key.
 */
public class Decrypt {
	private byte[]		key;
	private	Inflater	inflater;
	private JsonFactory	jsonFactory;
	private Calendar	calendar;
	
	/**
	 * Constructor
	 * 
	 * @param secretKey the value from company_tbl.secret_key
	 */
	public Decrypt (String secretKey) throws UnsupportedEncodingException {
		byte[]	input = secretKey.getBytes ("UTF-8");

		key = new byte[32];
		for (int n = 0; n < key.length; ++n) {
			key[n] = n < input.length ? input[n] : 0;
		}
		inflater = new Inflater ();
		jsonFactory = new JsonFactory ();
		calendar = Calendar.getInstance ();
	}

	/**
	 * decrypt a string provided by the backend for a customer
	 * 
	 * @param input      the encrypted input string
	 * @param customerID the ID of the customer to whom these data belongs
	 * @return           the decrypted value of the input, if decryptable
	 * @throws Exception
	 */
	public String decrypt (String input, long customerID) throws Exception {
		int	version = 0;
		boolean	compressed = false;
		
		if (input.startsWith ("V") && (input.length () > 1)) {
			char	versionID = input.charAt (1);
			
			switch (versionID) {
			case 'A':
			case 'a':
				version = 1;
				break;
			default:
				throw new Exception ("Unsupported version " + versionID + " found");
			}
			input = input.substring (2);
			compressed = Character.isLowerCase (versionID);
		}
		//
		byte[]	raw = null;
		switch (version) {
		case 0:
			raw = Hex.decodeHex (input);
			break;
		case 1:
			raw = Base64.decodeBase64 (input);
			break;
		}
		if (raw == null) {
			throw new Exception ("Failed to decode input string " + input + " for version " + version);
		}
		//
		byte[]	iv = new byte[16];

		for (int n = 0; n < iv.length; ++n) {
			iv[n] = (byte) (customerID & 0xff);
			customerID >>>= 8;
		}
		SecretKey	secKey = new SecretKeySpec (key, "AES");
		Cipher		cipher = Cipher.getInstance ("AES/CBC/PKCS5Padding");
		byte[]		content;

		cipher.init (Cipher.DECRYPT_MODE, secKey, new IvParameterSpec (iv));
		content = cipher.doFinal (raw);
		//
		if (compressed) {
			byte[]		buffer = new byte[raw.length * 2];
			
			inflater.reset ();
			inflater.setInput (content, 0, content.length);
			content = new byte[0];
			while (! inflater.finished ()) {
				int	length = inflater.inflate (buffer);
				
				if (length > 0) {
					byte[]	temp = new byte[content.length + length];
					
					if (content.length > 0) {
						System.arraycopy (content, 0, temp, 0, content.length);
					}
					System.arraycopy (buffer, 0, temp, content.length, length);
					content = temp;
				}
			}
		}
		//
		return new String (content, "UTF-8");
	}
	
	/**
	 * decrypts a string, assuming the input is json generated by
	 * the backend and parsing the json into a map with one of
	 * the value types number, string, date or null.
	 * 
	 * @param input      the encoded input string
	 * @param customerID the ID of the customer to whom these data belongs
	 * @return           the decoded value of the input, if decodable
	 * @throws Exception
	 */
	public Map <String, Object> decryptAndDecode (String input, long customerID) throws Exception {
		Map <String, Object>	rc = new HashMap <> ();
		
		try (JsonParser parser  = jsonFactory.createParser (decrypt (input, customerID))) {
			String	name = null;
			
			while (! parser.isClosed ()) {
				JsonToken	jsonToken = parser.nextToken ();

				if (jsonToken == JsonToken.FIELD_NAME) {
					name = parser.getValueAsString ();
				} else if (name != null) {
					if (jsonToken == JsonToken.VALUE_NULL) {
						rc.put (name, null);
					} else if ((jsonToken == JsonToken.VALUE_FALSE) || (jsonToken == JsonToken.VALUE_TRUE)) {
						rc.put (name, parser.getBooleanValue ());
					} else if ((jsonToken == JsonToken.VALUE_NUMBER_FLOAT) || (jsonToken == JsonToken.VALUE_NUMBER_INT)) {
						rc.put (name, parser.getNumberValue ());
					} else if (jsonToken == JsonToken.VALUE_STRING) {
						rc.put (name, parser.getText ());
					} else if (jsonToken == JsonToken.START_ARRAY) {
						int	indent = 1;
						int[]	date = new int[6];
						int	pos = 0;
						
						while ((! parser.isClosed ()) && (indent > 0)) {
							jsonToken = parser.nextToken ();
							
							if (jsonToken == JsonToken.START_ARRAY) {
								++indent;
							} else if (jsonToken == JsonToken.END_ARRAY) {
								--indent;
							} else if ((jsonToken == JsonToken.VALUE_NUMBER_FLOAT) || (jsonToken == JsonToken.VALUE_NUMBER_INT)) {
								if (pos < 6) {
									date[pos++] = (int) parser.getNumberValue ();
								}
							}
						}
						if (pos == 6) {
							calendar.set (date[0], date[1] - 1, date[2], date[3], date[4], date[5]);
							rc.put (name, calendar.getTime ());
						}
					} else if (jsonToken == JsonToken.START_OBJECT) {
						int	indent = 1;
						
						while ((! parser.isClosed ()) && (indent > 0)) {
							jsonToken = parser.nextToken ();
							
							if (jsonToken == JsonToken.START_OBJECT) {
								++indent;
							} else if (jsonToken == JsonToken.END_OBJECT) {
								--indent;
							}
						}
					}
					name = null;
				}
			}
		}
		return rc;
	}
}
