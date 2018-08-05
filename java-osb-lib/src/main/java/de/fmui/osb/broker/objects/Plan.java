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
import de.fmui.osb.broker.json.JSONObject;

@KeyMapping(jsonKey = "metadata", osbClass = PlanMetadata.class)
@KeyMapping(jsonKey = "schemas", osbClass = Schemas.class)
public class Plan extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public final static String KEY_NAME = "name";
	public final static String KEY_ID = "id";
	public final static String KEY_DESCRIPTION = "description";
	public final static String KEY_METADATA = "metadata";
	public final static String KEY_FREE = "free";
	public final static String KEY_BINDABLE = "bindable";
	public final static String KEY_SCHEMAS = "schemas";

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

	public PlanMetadata getMetadata() {
		return get(KEY_METADATA, PlanMetadata.class);
	}

	public void setMetadata(PlanMetadata metadata) {
		put(KEY_METADATA, metadata);
	}

	public Boolean getFree() {
		return getBoolean(KEY_FREE);
	}

	public void setFree(boolean free) {
		put(KEY_FREE, free);
	}

	public Boolean getBindable() {
		return getBoolean(KEY_BINDABLE);
	}

	public void setBindable(boolean bindable) {
		put(KEY_BINDABLE, bindable);
	}

	public Schemas getSchemas() {
		return get(KEY_SCHEMAS, Schemas.class);
	}

	public void setSchemas(Schemas schemas) {
		put(KEY_SCHEMAS, schemas);
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

		super.validate();
	}
}
