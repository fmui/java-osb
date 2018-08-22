/*
 * Copyright 2018 Florian Müller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package de.fmui.osb.broker.example.handler;

import java.io.IOException;
import java.io.InputStreamReader;

import de.fmui.osb.broker.BasicAuthCredentials;
import de.fmui.osb.broker.RequestCredentials;
import de.fmui.osb.broker.catalog.CatalogResponseBody;
import de.fmui.osb.broker.exceptions.UnauthorizedException;

public class BrokerUtils {

	/**
	 * Reads the catalog from a resource file.
	 */
	public static CatalogResponseBody readCatalogFromResourceFile(String path) throws IOException {
		CatalogResponseBody result = new CatalogResponseBody();

		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(BrokerUtils.class.getResourceAsStream(path), "UTF-8");
			result.load(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return result;
	}

	/**
	 * Checks if the credentials are basic auth and if username and password
	 * correct.
	 */
	public static void authenticate(RequestCredentials credentials, String username, String password)
			throws UnauthorizedException {
		// this broker requires basic auth
		if (!(credentials instanceof BasicAuthCredentials)) {
			throw new UnauthorizedException();
		}

		BasicAuthCredentials basicAuth = (BasicAuthCredentials) credentials;

		// check username and password
		if (!username.equals(basicAuth.getUsername()) || !password.equals(basicAuth.getPassword())) {
			throw new UnauthorizedException();
		}
	}
}
