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

public class CloudFoundryContext extends Context {

	private static final long serialVersionUID = 1L;

	public static final String KEY_ORGANIZATION_GUID = "organization_guid";
	public static final String KEY_SPACE_GUID = "space_guid";

	public CloudFoundryContext() {
	}

	public CloudFoundryContext(Context context) {
		putAll(context);
	}

	public String getOrganizationGUID() {
		return getString(KEY_ORGANIZATION_GUID);
	}

	public String getSpaceGUID() {
		return getString(KEY_SPACE_GUID);
	}
}
