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
package de.fmui.osb.broker;

/**
 * Last operation states.
 */
public enum State {
	IN_PROGRESS("in progres"), SUCCEEDED("succeeded"), FAILED("failed");

	private final String value;

	State(String v) {
		value = v;
	}

	/**
	 * Returns the state value as defined in the OSBAPI specification.
	 */
	public String value() {
		return value;
	}

	/**
	 * Returns the enum from the state value.
	 */
	public static State fromValue(String v) {
		for (State c : State.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
