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

import de.fmui.osb.broker.State;
import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.AbstractOpenServiceBrokerObject;

public class BindingLastOperationResponseBody extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_STATE = "state";
	public static final String KEY_DESCRIPTION = "description";

	public State getState() {
		String stateStr = getString(KEY_STATE);
		if (stateStr == null) {
			return null;
		}

		try {
			return State.fromValue(stateStr);
		} catch (Exception e) {
			throw new IllegalStateException("Invalid state value!");
		}
	}

	public void setState(State state) {
		put(KEY_STATE, state != null ? state.value() : null);
	}

	public String getDescription() {
		return getString(KEY_DESCRIPTION);
	}

	public void setDescription(String description) {
		put(KEY_DESCRIPTION, description);
	}

	@Override
	public void validate() throws ValidationException {
		if (getState() == null) {
			throw new ValidationException("State is not set!");
		}

		super.validate();
	}
}
