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

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.AbstractOpenServiceBrokerObject;
import de.fmui.osb.broker.objects.Context;
import de.fmui.osb.broker.objects.KeyMapping;
import de.fmui.osb.broker.objects.Parameters;

@KeyMapping(jsonKey = "context", osbClass = Context.class)
@KeyMapping(jsonKey = "parameters", osbClass = Parameters.class)
public class ProvisionRequestBody extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_CONTEXT = "context";
	public static final String KEY_SERVICE_ID = "service_id";
	public static final String KEY_PLAN_ID = "plan_id";
	public static final String KEY_ORGANIZATION_GUID = "organization_guid";
	public static final String KEY_SPACE_GUID = "space_guid";
	public static final String KEY_PARAMETERS = "parameters";

	public Context getContext() {
		return get(KEY_CONTEXT, Context.class);
	}

	/**
	 * Returns the service ID.
	 */
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
	public String getOrganizationGUID() {
		return getString(KEY_ORGANIZATION_GUID);
	}

	@Deprecated
	public String getSpaceGUID() {
		return getString(KEY_SPACE_GUID);
	}

	public Parameters getParameters() {
		return get(KEY_PARAMETERS, Parameters.class);
	}

	@Override
	public void validate() throws ValidationException {
		if (!isValidID(KEY_SERVICE_ID)) {
			throw new ValidationException("Invalid or missing service ID!");
		}
		if (!isValidID(KEY_PLAN_ID)) {
			throw new ValidationException("Invalid or missing plan ID!");
		}
		if (!isValidID(KEY_ORGANIZATION_GUID)) {
			throw new ValidationException("Invalid or missing organization GUID!");
		}
		if (!isValidID(KEY_SPACE_GUID)) {
			throw new ValidationException("Invalid or missing space GUID!");
		}

		super.validate();
	}
}
