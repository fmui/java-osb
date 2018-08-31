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
import de.fmui.osb.broker.objects.PreviousValues;

@KeyMapping(jsonKey = "context", osbClass = Context.class)
@KeyMapping(jsonKey = "parameters", osbClass = Parameters.class)
@KeyMapping(jsonKey = "previous_values", osbClass = PreviousValues.class)
public class UpdateServiceInstanceRequestBody extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_CONTEXT = "context";
	public static final String KEY_SERVICE_ID = "service_id";
	public static final String KEY_PLAN_ID = "plan_id";
	public static final String KEY_PARAMETERS = "parameters";
	public static final String KEY_PREVIOUS_VALUES = "previous_values";

	public Context getContext() {
		return get(KEY_CONTEXT, Context.class);
	}

	/**
	 * Returns the service ID.
	 * 
	 * @return the service ID
	 */
	public String getServiceID() {
		return getString(KEY_SERVICE_ID);
	}

	/**
	 * Returns the plan ID.
	 * 
	 * @return the plan ID
	 */
	public String getPlanID() {
		return getString(KEY_PLAN_ID);
	}

	public Parameters getParameters() {
		return get(KEY_PARAMETERS, Parameters.class);
	}

	public PreviousValues getPreviousValues() {
		return get(KEY_PREVIOUS_VALUES, PreviousValues.class);
	}

	@Override
	public void validate() throws ValidationException {
		if (!isValidID(KEY_SERVICE_ID)) {
			throw new ValidationException("Invalid or missing service ID!");
		}

		super.validate();
	}
}
