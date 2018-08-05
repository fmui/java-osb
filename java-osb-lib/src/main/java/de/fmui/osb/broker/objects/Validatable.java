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

public interface Validatable {

	/**
	 * Check if the object and its subobjects are in valid state.
	 * 
	 * @throws ValidationException
	 *             if the object is not in an valid state
	 */
	void validate() throws ValidationException;
}
