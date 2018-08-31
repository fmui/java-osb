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

import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.AbstractOpenServiceBrokerObject;
import de.fmui.osb.broker.objects.Parameters;

public class FetchInstanceResponseBody extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_SERVICE_ID = "service_id";
	public static final String KEY_PLAN_ID = "plan_id";
	public static final String KEY_DASHBOARD_URL = "dashboard_url";
	public static final String KEY_PARAMETERS = "parameters";

	/**
	 * Gets the service ID.
	 * 
	 * @return the service ID
	 */
	public String getServiceID() {
		return getString(KEY_SERVICE_ID);
	}

	public void setServiceID(String id) {
		put(KEY_SERVICE_ID, id);
	}

	/**
	 * Gets the plan ID.
	 * 
	 * @return the plan ID
	 */
	public String getPlanID() {
		return getString(KEY_PLAN_ID);
	}

	public void setPlanID(String id) {
		put(KEY_PLAN_ID, id);
	}

	public String getDashboardURL() {
		return getString(KEY_DASHBOARD_URL);
	}

	public void setDashboardURL(String dasboardURL) {
		put(KEY_DASHBOARD_URL, dasboardURL);
	}

	public Parameters getParameters() {
		return get(KEY_PARAMETERS, Parameters.class);
	}

	public void setParameters(Parameters parameters) {
		put(KEY_PARAMETERS, parameters);
	}
}
