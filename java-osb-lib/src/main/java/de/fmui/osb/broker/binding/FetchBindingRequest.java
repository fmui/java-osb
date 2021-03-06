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
package de.fmui.osb.broker.binding;

import de.fmui.osb.broker.OpenServiceBrokerRequest;

public class FetchBindingRequest extends OpenServiceBrokerRequest {

	private String instanceID;
	private String bindingID;

	public FetchBindingRequest(String instanceID, String bindingID) {
		this.instanceID = instanceID;
		this.bindingID = bindingID;
	}

	/**
	 * Returns the instance ID.
	 * 
	 * @return the service instance ID
	 */
	public String getInstanceID() {
		return instanceID;
	}

	/**
	 * Returns the binding ID.
	 * 
	 * @return the service binding ID
	 */
	public String getBindingID() {
		return bindingID;
	}
}