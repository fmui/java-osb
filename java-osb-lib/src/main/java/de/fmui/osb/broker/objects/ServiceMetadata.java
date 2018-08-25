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

public class ServiceMetadata extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public final static String KEY_DISPLAY_NAME = "displayName";
	public final static String KEY_IMAGE_URL = "imageUrl";
	public final static String KEY_LONG_DESCRIPTION = "longDescription";
	public final static String KEY_PROVIDER_DISPLAY_NAME = "providerDisplayName";
	public final static String KEY_DOCUMENTATION_URL = "documentationUrl";
	public final static String KEY_SUPPORT_URL = "supportUrl";

	public String getDisplayName() {
		return getString(KEY_DISPLAY_NAME);
	}

	public void setDisplayName(String name) {
		put(KEY_DISPLAY_NAME, name);
	}

	public String getImageURL() {
		return getString(KEY_IMAGE_URL);
	}

	public void setImageURL(String url) {
		put(KEY_IMAGE_URL, url);
	}

	public String getLongDescription() {
		return getString(KEY_LONG_DESCRIPTION);
	}

	public void setLongDescription(String description) {
		put(KEY_LONG_DESCRIPTION, description);
	}

	public String getProviderDisplayName() {
		return getString(KEY_PROVIDER_DISPLAY_NAME);
	}

	public void setProviderDisplayName(String description) {
		put(KEY_PROVIDER_DISPLAY_NAME, description);
	}

	public String getDocumentationURL() {
		return getString(KEY_DOCUMENTATION_URL);
	}

	public void setDocumentationURL(String url) {
		put(KEY_DOCUMENTATION_URL, url);
	}

	public String getSupportURL() {
		return getString(KEY_SUPPORT_URL);
	}

	public void setSupportURL(String url) {
		put(KEY_SUPPORT_URL, url);
	}
}