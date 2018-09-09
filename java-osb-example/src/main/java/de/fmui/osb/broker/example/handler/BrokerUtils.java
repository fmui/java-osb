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

import de.fmui.osb.broker.BasicAuthCredentials;
import de.fmui.osb.broker.RequestCredentials;
import de.fmui.osb.broker.exceptions.UnauthorizedException;

public class BrokerUtils {

	/**
	 * Checks if the credentials are basic auth and if user name and password
	 * correct.
	 * 
	 * @param credentials
	 *            the credentials to check
	 * @param username
	 *            the user name
	 * @param password
	 *            the password
	 * 
	 * @throws UnauthorizedException
	 *             if the credentials don't match user name and password
	 */
	public static void authenticate(RequestCredentials credentials, String username, String password)
			throws UnauthorizedException {
		// this broker requires basic auth
		if (!credentials.isBasicAuthentication()) {
			throw new UnauthorizedException();
		}

		BasicAuthCredentials basicAuth = (BasicAuthCredentials) credentials;

		// check username and password
		if (!username.equals(basicAuth.getUsername()) || !password.equals(basicAuth.getPassword())) {
			throw new UnauthorizedException();
		}
	}
}
