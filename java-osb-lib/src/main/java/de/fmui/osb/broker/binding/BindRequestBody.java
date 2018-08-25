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
package de.fmui.osb.broker.binding;

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.AbstractOpenServiceBrokerObject;
import de.fmui.osb.broker.objects.BindResource;
import de.fmui.osb.broker.objects.Context;
import de.fmui.osb.broker.objects.KeyMapping;
import de.fmui.osb.broker.objects.Parameters;

@KeyMapping(jsonKey = "context", osbClass = Context.class)
@KeyMapping(jsonKey = "bind_resource", osbClass = BindResource.class)
@KeyMapping(jsonKey = "parameters", osbClass = Parameters.class)
public class BindRequestBody extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_CONTEXT = "context";
	public static final String KEY_SERVICE_ID = "service_id";
	public static final String KEY_PLAN_ID = "plan_id";
	public static final String KEY_APP_GUID = "app_guid";
	public static final String KEY_BIND_RESOURCE = "bind_resource";
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

	/**
	 * Returns the app GUID.
	 * 
	 * @deprecated see {@link BindResource}
	 */
	@Deprecated
	public String getAppGUID() {
		return getString(KEY_APP_GUID);
	}

	/**
	 * Returns the bind resource.
	 */
	public BindResource getBindResource() {
		return get(KEY_BIND_RESOURCE, BindResource.class);
	}

	/**
	 * Returns the configuration parameters.
	 */
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

		super.validate();
	}
}
