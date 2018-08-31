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

import de.fmui.osb.broker.json.JSONObject;

/**
 * Generic Open Service Broker response.
 */
public abstract class OpenServiceBrokerResponse {

	protected int statusCode;
	protected JSONObject body;

	/**
	 * Returns the HTTP status code.
	 * 
	 * @return the HTTP status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns the response body.
	 * 
	 * @return the response body or {@code null} if there is no response body
	 */
	public JSONObject getResponseBody() {
		return body;
	}

	@Override
	public String toString() {
		return "Status Code: " + getStatusCode() + "\nBody: " + body;
	}
}
