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
package de.fmui.osb.broker.exceptions;

import de.fmui.osb.broker.internal.json.JSONObjectImpl;

/**
 * Base Open Service Broker exception class.
 */
public class OpenServiceBrokerException extends Exception {
	private static final long serialVersionUID = 1L;

	private int statusCode;
	private String error;
	private String description;

	public OpenServiceBrokerException() {
		super();
	}

	public OpenServiceBrokerException(int statusCode, String error, String description) {
		super(description);
		this.statusCode = statusCode;
		this.error = error;
		this.description = description;
	}

	public OpenServiceBrokerException(int statusCode, String error, String description, Throwable cause) {
		super(error + ": " + description, cause);
		this.statusCode = statusCode;
		this.error = error;
		this.description = description;
	}

	public OpenServiceBrokerException(String message) {
		this(500, "Error", message);
	}

	public OpenServiceBrokerException(Throwable cause) {
		this(500, "Error", "", cause);
	}

	public OpenServiceBrokerException(String message, Throwable cause) {
		this(500, "Error", message, cause);
	}

	/**
	 * Returns the HTTP status code.
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns the error code.
	 */
	public String getError() {
		return error;
	}

	/**
	 * Returns the error description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the JSON representation of the exception.
	 */
	public String toJSONString() {
		JSONObjectImpl json = new JSONObjectImpl();

		if (error != null) {
			json.put("error", error);
		}
		if (description != null) {
			json.put("description", description);
		}

		return json.toJSONString();
	}
}
