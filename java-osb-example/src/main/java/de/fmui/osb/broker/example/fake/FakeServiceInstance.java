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
package de.fmui.osb.broker.example.fake;

import java.util.HashMap;
import java.util.Map;

import de.fmui.osb.broker.instance.ProvisionRequest;

public class FakeServiceInstance {

	private String id;
	private String serviceID;
	private String planID;
	private Map<String, Object> context;
	private Map<String, Object> parameters;
	private Map<String, FakeServiceBinding> bindings = new HashMap<>();

	private long provisioningTimestamp = 0;
	private long deprovisioningTimestamp = 0;

	public FakeServiceInstance(String id) {
		this.id = id;
	}

	public FakeServiceInstance(ProvisionRequest request) {
		this(request.getInstanceID());
		serviceID = request.getRequestBody().getServiceID();
		planID = request.getRequestBody().getPlanID();
		context = request.getRequestBody().getContext();
		parameters = request.getRequestBody().getParameters();
	}

	public String getId() {
		return id;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getPlanID() {
		return planID;
	}

	public void setPlanID(String planID) {
		this.planID = planID;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public synchronized void addServiceBinding(FakeServiceBinding binding) {
		bindings.put(binding.getId(), binding);
	}

	public synchronized FakeServiceBinding getServiceBinding(String bindingID) {
		return bindings.get(bindingID);
	}

	public synchronized boolean removeServiceBinding(String bindingID) {
		return bindings.remove(bindingID) != null;
	}

	public long getProvisioningTimestamp() {
		return provisioningTimestamp;
	}

	public void setProvisioningTimestamp(long provisioningTimestamp) {
		this.provisioningTimestamp = provisioningTimestamp;
	}

	public boolean isProvisioningInProgress() {
		return provisioningTimestamp > 0 && provisioningTimestamp >= System.currentTimeMillis();
	}

	public long getDeprovisioningTimestamp() {
		return deprovisioningTimestamp;
	}

	public void setDeprovisioningTimestamp(long deprovisioningTimestamp) {
		this.deprovisioningTimestamp = deprovisioningTimestamp;
	}

	public boolean isDeprovisioningInProgress() {
		return deprovisioningTimestamp > 0 && deprovisioningTimestamp >= System.currentTimeMillis();
	}

	public boolean isGone() {
		return deprovisioningTimestamp > 0 && deprovisioningTimestamp < System.currentTimeMillis();
	}
}
