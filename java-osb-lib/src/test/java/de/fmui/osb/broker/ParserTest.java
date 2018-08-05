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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fmui.osb.broker.instance.ProvisionRequestBody;
import de.fmui.osb.broker.internal.json.parser.JSONParser;

public class ParserTest {

	@Test
	public void testParse() throws Exception {
		String context = "\"context\": {\"key\": \"value\", \"platform\": \"mypf\"}";
		String keyValue1 = "\"test1\": \"abc\"";
		String keyValue2 = "\"test2\": 123";
		String keyValue3 = "\"tes3\": false";
		String array = "\"array\": [\"a1\",\"b2\", \"c3\"]";
		String json = "{ " + context + ", " + keyValue1 + ", " + keyValue2 + ", " + keyValue3 + "," + array + "}";
		JSONParser parser = new JSONParser();
		ProvisionRequestBody obj = parser.parse(json, new ProvisionRequestBody());
		assertTrue(obj.getContext() != null);
		assertEquals("value", obj.getContext().get("key"));
	}
}
