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

import java.util.Map;

import de.fmui.osb.broker.binding.BindRequest;
import de.fmui.osb.broker.objects.Credentials;

public class FakeServiceBinding {

	private String id;
	private String serviceID;
	private String planID;
	private Map<String, Object> context;
	private Map<String, Object> bindingResource;
	private Map<String, Object> parameters;

	private Credentials credentials;

	public FakeServiceBinding(String id) {
		this.id = id;
	}

	public FakeServiceBinding(BindRequest request) {
		id = request.getBindingID();
		serviceID = request.getRequestBody().getServiceID();
		planID = request.getRequestBody().getPlanID();
		context = request.getRequestBody().getContext();
		bindingResource = request.getRequestBody().getBindResource();
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

	public Map<String, Object> getBindingResource() {
		return bindingResource;
	}

	public void setBindingResource(Map<String, Object> bindingResource) {
		this.bindingResource = bindingResource;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
}
