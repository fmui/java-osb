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

public class DeprovisionRequest extends OpenServiceBrokerRequest {

	private String instanceID;
	private String serviceID;
	private String planID;

	public DeprovisionRequest(String instanceID, String serviceID, String planID) {
		this.instanceID = instanceID;
		this.serviceID = serviceID;
		this.planID = planID;
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
	 * Returns the service ID.
	 * 
	 * @return the service ID
	 */
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * Returns the plan ID.
	 * 
	 * @return the plan ID
	 */
	public String getPlanID() {
		return planID;
	}
}
