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
package de.fmui.osb.broker.instance;

import de.fmui.osb.broker.OpenServiceBrokerRequest;

public class ProvisionRequest extends OpenServiceBrokerRequest {

	private String instanceID;

	public ProvisionRequest(String instanceID) {
		this.instanceID = instanceID;
	}

	/**
	 * Returns the instance ID.
	 */
	public String getInstanceID() {
		return instanceID;
	}

	@Override
	public ProvisionRequestBody getRequestBody() {
		return (ProvisionRequestBody) super.getRequestBody();
	}
}
