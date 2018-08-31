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
	 * 
	 * @return the Broker API Version object
	 */
	public BrokerAPIVersion getBrokerAPIVersion() {
		return brokerAPIVersion;
	}

	void setBrokerAPIVersion(BrokerAPIVersion brokerAPIVersion) {
		this.brokerAPIVersion = brokerAPIVersion;
	}

	/**
	 * Returns if the {@code accepts_ncomplete} query parameter is set to
	 * {@code true}.
	 * 
	 * @return {@code true} if the {@code accepts_ncomplete} query parameter is set
	 *         to {@code true}, {@code false} otherwise
	 */
	public boolean isAcceptsIncomplete() {
		return acceptsIncomplete;
	}

	void setAcceptsIncomplete(boolean acceptsIncomplete) {
		this.acceptsIncomplete = acceptsIncomplete;
	}

	/**
	 * Returns Originating Identity sent by the platform.
	 * 
	 * @return the Originating Identity object
	 */
	public OriginatingIdentity getOriginatingIdentity() {
		return originatingIdentity;
	}

	void setOriginatingIdentity(OriginatingIdentity originatingIdentity) {
		this.originatingIdentity = originatingIdentity;
	}

	/**
	 * Gets the credentials provided by the platform.
	 * 
	 * @return a {@link RequestCredentials} object
	 */
	public RequestCredentials getCredentials() {
		return credentials;
	}

	void setCredentials(RequestCredentials credentials) {
		this.credentials = credentials;
	}

	/**
	 * Returns the body of the request.
	 * 
	 * @return the request body or {@code null} if there is no request body
	 */
	public JSONObject getRequestBody() {
		return body;
	}

	void setRequestBody(JSONObject body) {
		this.body = body;
	}

	/**
	 * Returns the HTTP request object.
	 * 
	 * @return the HTTP request object
	 */
	public HttpServletRequest getHttpServletRequest() {
		return request;
	}

	void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return "Request: " + request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() == null ? "" : "?" + request.getRequestURI()) + "\n" + //
				"Broker API Version: " + brokerAPIVersion + "\n" + //
				"Originating Identity: " + (originatingIdentity == null ? "-"
						: originatingIdentity.getPlatform() + " " + originatingIdentity.getValueAsJSON())
				+ "\n" + //
				"Body: " + body;
	}
}
