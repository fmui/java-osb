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
package de.fmui.osb.broker.objects;

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.json.JSONArray;
import de.fmui.osb.broker.json.JSONObject;

@KeyMapping(jsonKey = "metadata", osbClass = ServiceMetadata.class)
@KeyMapping(jsonKey = "dashboard_client", osbClass = DashboardClient.class)
@KeyMapping(jsonKey = "plans", osbClass = Plan.class)
public class Service extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public final static String KEY_NAME = "name";
	public final static String KEY_ID = "id";
	public final static String KEY_DESCRIPTION = "description";
	public final static String KEY_TAGS = "tags";
	public final static String KEY_REQUIRES = "requires";
	public final static String KEY_BINDABLE = "bindable";
	public final static String KEY_INSTANCE_RETRIEVABLE = "instances_retrievable";
	public final static String KEY_BINDING_RETRIEVABLE = "bindings_retrievable";
	public final static String KEY_METADATA = "metadata";
	public final static String KEY_DASHBOARD_CLIENT = "dashboard_client";
	public final static String KEY_PLAN_UPDATABLE = " plan_updateable";
	public final static String KEY_PLANS = "plans";

	public String getName() {
		return getString(KEY_NAME);
	}

	public void setName(String name) {
		put(KEY_NAME, name);
	}

	public String getID() {
		return getString(KEY_ID);
	}

	public void setID(String id) {
		put(KEY_ID, id);
	}

	public String getDescription() {
		return getString(KEY_DESCRIPTION);
	}

	public void setDescription(String description) {
		put(KEY_DESCRIPTION, description);
	}

	public JSONArray<String> getTags() {
		return getArray(KEY_TAGS, String.class);
	}

	public void setTags(String... tags) {
		createArray(KEY_TAGS, String.class, tags);
	}

	public void addTag(String... tag) {
		addToArray(KEY_TAGS, String.class, tag);
	}

	public JSONArray<String> getRequires() {
		return getArray(KEY_REQUIRES, String.class);
	}

	public void setRequires(String... tags) {
		createArray(KEY_REQUIRES, String.class, tags);
	}

	public void addRequires(String... tags) {
		addToArray(KEY_REQUIRES, String.class, tags);
	}

	public Boolean getBindable() {
		return getBoolean(KEY_BINDABLE);
	}

	public void setBindable(boolean bindable) {
		put(KEY_BINDABLE, bindable);
	}

	public Boolean getInstancesRetrievable() {
		return getBoolean(KEY_INSTANCE_RETRIEVABLE);
	}

	public void setInstancesRetrievable(boolean retrievable) {
		put(KEY_INSTANCE_RETRIEVABLE, retrievable);
	}

	public Boolean getBindingsRetrievable() {
		return getBoolean(KEY_BINDING_RETRIEVABLE);
	}

	public void setBindingsRetrievable(boolean retrievable) {
		put(KEY_BINDING_RETRIEVABLE, retrievable);
	}

	public ServiceMetadata getMetadata() {
		return get(KEY_METADATA, ServiceMetadata.class);
	}

	public void setMetadata(ServiceMetadata metadata) {
		put(KEY_METADATA, metadata);
	}

	public DashboardClient getDashboardClient() {
		return get(KEY_DASHBOARD_CLIENT, DashboardClient.class);
	}

	public void setDashboardClient(DashboardClient client) {
		put(KEY_DASHBOARD_CLIENT, client);
	}

	public Boolean getPlanUpdateable() {
		return getBoolean(KEY_PLAN_UPDATABLE);
	}

	public void setPlanUpdateable(boolean updateable) {
		put(KEY_PLAN_UPDATABLE, updateable);
	}

	public JSONArray<Plan> getPlans() {
		return getArray(KEY_PLANS, Plan.class);
	}

	public void setPlans(Plan... plans) {
		createArray(KEY_PLANS, Plan.class, plans);
	}

	public void addPlan(Plan... plan) {
		addToArray(KEY_PLANS, Plan.class, plan);
	}

	@Override
	public void validate() throws ValidationException {
		if (!isValidName(KEY_NAME)) {
			throw new ValidationException("Invalid or missing name!");
		}
		if (!isValidID(KEY_ID)) {
			throw new ValidationException("Invalid or missing ID!");
		}
		if (isNullOrEmpty(KEY_DESCRIPTION)) {
			throw new ValidationException("Invalid or missing description!");
		}
		if (getBindable() == null) {
			throw new ValidationException("Invalid or missing bindable flag!");
		}

		super.validate();
	}
}
