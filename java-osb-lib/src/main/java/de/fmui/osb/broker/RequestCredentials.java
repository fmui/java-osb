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

/**
 * Request credentials.
 */
public class RequestCredentials {

	private HttpServletRequest request;

	/**
	 * Constructor.
	 * 
	 * @param request
	 *            the HTTP request object
	 */
	RequestCredentials(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Returns the HTTP request object.
	 * 
	 * @return the HTTP request object
	 */
	public HttpServletRequest getHttpServletRequest() {
		return request;
	}

	/**
	 * Returns if basic authentication is used.
	 * 
	 * @return {@code true} if basic authentication is used, {@code false} otherwise
	 */
	public boolean isBasicAuthentication() {
		return false;
	}

	/**
	 * Returns if token authentication is used.
	 * 
	 * @return {@code true} if token authentication is used, {@code false} otherwise
	 */
	public boolean isTokenAuthentication() {
		return false;
	}

}
