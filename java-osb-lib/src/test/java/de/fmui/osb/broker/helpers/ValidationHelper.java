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
package de.fmui.osb.broker.helpers;

import static org.junit.Assert.fail;

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.objects.Validatable;

public class ValidationHelper {

	public static void assertValidating(Validatable validatable) {
		if (validatable == null) {
			fail();
		}

		try {
			validatable.validate();
		} catch (ValidationException ve) {
			fail("Validation should pass.");
		}
	}

	public static void assertNotValidating(Validatable validatable) {
		if (validatable == null) {
			fail();
		}

		try {
			validatable.validate();
			fail("Validation should fail.");
		} catch (ValidationException ve) {
			// expected
		}
	}
}
