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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.internal.json.JSONValue;

public class OriginatingIdentityTest {

	@Test
	public void testParse() throws Exception {
		OriginatingIdentity oi = new OriginatingIdentity(
				"someplatform eyANCiAgInVzZXJfaWQiOiAiNjgzZWE3NDgtMzA5Mi00ZmY0LWI2NTYtMzljYWNjNGQ1MzYwIiwNCiAgInVzZXJfbmFtZSI6ICJqb2VAZXhhbXBsZS5jb20iDQp9");

		assertEquals("someplatform", oi.getPlatform());
		assertEquals("683ea748-3092-4ff4-b656-39cacc4d5360", oi.getValueAsJSON().get("user_id"));
		assertEquals("683ea748-3092-4ff4-b656-39cacc4d5360",
				JSONValue.parseWithException(oi.getValue(), new JSONObjectImpl()).get("user_id"));
	}
}
