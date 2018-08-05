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

	@Override
	public void validate() {
		// there is only a convention for metadata fields, no specification -> nothing
		// to do
	}
}
