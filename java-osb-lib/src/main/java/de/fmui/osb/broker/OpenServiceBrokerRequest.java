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

import de.fmui.osb.broker.json.JSONObject;

/**
 * Generic Open Service Broker request.
 */
public abstract class OpenServiceBrokerRequest {
	private BrokerAPIVersion brokerAPIVersion;
	private boolean acceptsIncomplete;
	private OriginatingIdentity originatingIdentity;
	private RequestCredentials credentials;
	private JSONObject body;
	private HttpServletRequest request;

	/**
	 * Returns Broker API Version sent by the platform.
	 */
	public BrokerAPIVersion getBrokerAPIVersion() {
		return brokerAPIVersion;
	}

	void setBrokerAPIVersion(BrokerAPIVersion brokerAPIVersion) {
		this.brokerAPIVersion = brokerAPIVersion;
	}

	/**
	 * Returns {@code true} if the {@code accepts_ncomplete} query parameter is set
	 * to {@code true}.
	 */
	public boolean isAcceptsIncomplete() {
		return acceptsIncomplete;
	}

	void setAcceptsIncomplete(boolean acceptsIncomplete) {
		this.acceptsIncomplete = acceptsIncomplete;
	}

	/**
	 * Returns Originating Identity sent by the platform.
	 */
	public OriginatingIdentity getOriginatingIdentity() {
		return originatingIdentity;
	}

	void setOriginatingIdentity(OriginatingIdentity originatingIdentity) {
		this.originatingIdentity = originatingIdentity;
	}

	/**
	 * Returns the credentials.
	 */
	public RequestCredentials getCredentials() {
		return credentials;
	}

	void setCredentials(RequestCredentials credentials) {
		this.credentials = credentials;
	}

	/**
	 * Returns the body of the request.
	 */
	public JSONObject getRequestBody() {
		return body;
	}

	void setRequestBody(JSONObject body) {
		this.body = body;
	}

	/**
	 * Returns the HTTP request object.
	 */
	public HttpServletRequest getHttpServletRequest() {
		return request;
	}

	void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
