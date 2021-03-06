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

/**
 * Invalid Broker API Version Header exception.
 * 
 * <p>
 * HTTP status code: <em>412</em><br>
 * </p>
 */
public class InvalidBrokerAPIVersionHeader extends OpenServiceBrokerException {

	private static final long serialVersionUID = 1L;

	public InvalidBrokerAPIVersionHeader(String description) {
		super(412, "InvalidBrokerAPIVersion", description);
	}

	public InvalidBrokerAPIVersionHeader(String description, Throwable cause) {
		super(412, "InvalidBrokerAPIVersion", description, cause);
	}
}
