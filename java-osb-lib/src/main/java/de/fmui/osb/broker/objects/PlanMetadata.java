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

import de.fmui.osb.broker.json.JSONArray;
import de.fmui.osb.broker.json.JSONObject;

public class PlanMetadata extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public final static String KEY_BULLETS = "bullets";
	public final static String KEY_COSTS = "costs";
	public final static String KEY_DISPLAY_NAME = "displayName";

	public JSONArray<String> getBullets() {
		return getArray(KEY_BULLETS, String.class);
	}

	public void setBullets(String... bullets) {
		createArray(KEY_BULLETS, String.class, bullets);
	}

	public void addBullet(String... bullet) {
		addToArray(KEY_BULLETS, String.class, bullet);
	}

	public JSONArray<JSONObject> getCosts() {
		return getArray(KEY_COSTS, JSONObject.class);
	}

	public void setCosts(JSONObject... costs) {
		createArray(KEY_COSTS, JSONObject.class, costs);
	}

	public void addCost(JSONObject... cost) {
		addToArray(KEY_COSTS, JSONObject.class, cost);
	}

	public String getDisplayName() {
		return getString(KEY_DISPLAY_NAME);
	}

	public void setDisplayName(String name) {
		put(KEY_DISPLAY_NAME, name);
	}
}
