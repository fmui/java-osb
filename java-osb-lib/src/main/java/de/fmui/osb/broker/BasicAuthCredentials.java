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
package de.fmui.osb.broker;

import javax.servlet.http.HttpServletRequest;

import de.fmui.osb.broker.internal.io.HttpUtils;
import de.fmui.osb.broker.internal.io.IOUtils;

/**
 * Basic Authentication credentials.
 */
public class BasicAuthCredentials extends RequestCredentials {

	private String user;
	private String password;

	public BasicAuthCredentials(HttpServletRequest request, String user, String password) {
		super(request);
		this.user = user;
		this.password = password;
	}

	/**
	 * Returns the user name.
	 */
	public String getUsername() {
		return user;
	}

	/**
	 * Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Creates a {@link BasicAuthCredentials} object if the request has basic auth
	 * credentials.
	 */
	public static BasicAuthCredentials createCredentialsFromRequest(HttpServletRequest request) {
		String[] authHeader = HttpUtils.splitAuthHeader(request);
		if (authHeader != null) {
			if (authHeader[0].equalsIgnoreCase("basic")) {
				String credentials = IOUtils.decodeBase64ISOString(authHeader[1]);
				int x = credentials.indexOf(':');
				if (x == -1) {
					return new BasicAuthCredentials(request, credentials, "");
				} else {
					return new BasicAuthCredentials(request, credentials.substring(0, x), credentials.substring(x + 1));
				}
			}
		}

		return null;
	}
}
