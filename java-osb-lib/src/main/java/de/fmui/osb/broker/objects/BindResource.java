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

public class BindResource extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_APP_GUID = "app_guid";
	public static final String KEY_ROUTE = "route";

	public String getAppGUID() {
		return getString(KEY_APP_GUID);
	}

	public void setAppGUID(String appGUID) {
		put(KEY_APP_GUID, appGUID);
	}

	public String getRoute() {
		return getString(KEY_ROUTE);
	}

	public void setAppRoute(String route) {
		put(KEY_ROUTE, route);
	}
}
