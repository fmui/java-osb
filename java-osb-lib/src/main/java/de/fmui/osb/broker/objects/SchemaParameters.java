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
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.json.JSONObject;

public class SchemaParameters extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public final static String KEY_SCHEMA_DECLARATION = "$schema";
	public final static String KEY_TYPE = "type";
	public final static String KEY_PROPERTIES = "properties";

	public final static String JSON_SCHEMA_DRAFT_V4 = "http://json-schema.org/draft-04/schema#";

	public String getSchemaDeclaration() {
		return getString(KEY_SCHEMA_DECLARATION);
	}

	public void setSchemaDeclaration(String urn) {
		put(KEY_SCHEMA_DECLARATION, urn);
	}

	public String getType() {
		return getString(KEY_TYPE);
	}

	public void setType(String type) {
		put(KEY_TYPE, type);
	}

	public JSONObject getProperties() {
		return getJSONObject(KEY_PROPERTIES);
	}

	public void setProperties(JSONObject properties) {
		put(KEY_PROPERTIES, properties);
	}

	public JSONObject addProperty(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Invalid property name!");
		}

		JSONObject properties = getProperties();
		if (properties == null) {
			properties = createObject(KEY_PROPERTIES);
		}

		JSONObject property = new JSONObjectImpl();
		properties.put(name, property);

		return property;
	}

	public void setDefaults() {
		setSchemaDeclaration(JSON_SCHEMA_DRAFT_V4);
		setType("object");
		createObject(KEY_PROPERTIES);
	}

	@Override
	public void validate() throws ValidationException {
		if (isNullOrEmpty(KEY_SCHEMA_DECLARATION)) {
			throw new ValidationException("Invalid or missing schema declaration!");
		}
	}
}
