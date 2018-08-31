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

/**
 * Bearer Authentication credentials.
 */
public class BearerCredentials extends RequestCredentials {

	private String token;

	/**
	 * Constructor.
	 * 
	 * @param request
	 *            the HTTP request object
	 * @param token
	 *            the token
	 */
	public BearerCredentials(HttpServletRequest request, String token) {
		super(request);
		this.token = token;
	}

	/**
	 * Returns the token.
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Creates a {@link BearerCredentials} object if the request has a bearer token.
	 * 
	 * @param request
	 *            the HTTP request object
	 * 
	 * @return a {@link BearerCredentials} or {@code null} if the request does not
	 *         contain a bearer token
	 */
	public static BearerCredentials createCredentialsFromRequest(HttpServletRequest request) {
		String[] authHeader = HttpUtils.splitAuthHeader(request);
		if (authHeader != null) {
			if (authHeader[0].equalsIgnoreCase("bearer")) {
				return new BearerCredentials(request, authHeader[1]);
			}
		}

		return null;
	}
}
