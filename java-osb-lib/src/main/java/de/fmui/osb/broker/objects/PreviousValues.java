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

import de.fmui.osb.broker.json.JSONObject;

public class PreviousValues extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_SERVICE_ID = "service_id";
	public static final String KEY_PLAN_ID = "plan_id";
	public static final String KEY_ORGANIZATION_ID = "organization_id";
	public static final String KEY_SPACE_ID = "space_id";

	/**
	 * Returns the service ID.
	 */
	@Deprecated
	public String getServiceID() {
		return getString(KEY_SERVICE_ID);
	}

	/**
	 * Returns the plan ID.
	 */
	public String getPlanID() {
		return getString(KEY_PLAN_ID);
	}

	@Deprecated
	public String getOrganizationID() {
		return getString(KEY_ORGANIZATION_ID);
	}

	@Deprecated
	public String getSpaceID() {
		return getString(KEY_SPACE_ID);
	}
}
