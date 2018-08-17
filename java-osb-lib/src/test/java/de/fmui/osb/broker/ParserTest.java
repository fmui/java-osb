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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.fmui.osb.broker.instance.ProvisionRequestBody;
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.internal.json.JSONValue;
import de.fmui.osb.broker.internal.json.parser.JSONParser;
import de.fmui.osb.broker.json.JSONObject;

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

	@Test
	public void testParse2() throws Exception {
		String charTestStr = "\\\\\n\t\r\"\f\b\u0000";

		JSONObject json = new JSONObjectImpl();
		json.put("null", null);
		json.put("integer", 42);
		json.put("double", 42.1d);
		json.put("float", 42.2f);
		json.put("dec", new BigDecimal("42.3"));
		json.put("bool", true);
		json.put("chars", charTestStr);
		json.put("list", Arrays.asList("a", "b", "c"));
		json.put("map", Collections.singletonMap("key", "value"));

		String jsonStr1 = json.toJSONString();
		assertNotNull(jsonStr1);

		StringWriter sw = new StringWriter();
		json.writeJSONString(sw);
		String jsonStr2 = sw.toString();
		assertNotNull(jsonStr2);

		assertEquals(jsonStr1, jsonStr2);

		JSONObject parsed = JSONValue.parse(jsonStr1, new JSONObjectImpl());
		assertEquals(null, parsed.get("null"));
		assertTrue(parsed.containsKey("null"));
		assertEquals(42L, parsed.get("integer"));
		assertTrue(parsed.get("double") instanceof BigDecimal);
		assertTrue(parsed.get("float") instanceof BigDecimal);
		assertTrue(parsed.get("dec") instanceof BigDecimal);
		assertEquals(new BigDecimal("42.3"), parsed.get("dec"));
		assertEquals(true, parsed.get("bool"));
		assertEquals(charTestStr, parsed.get("chars"));
		assertTrue(parsed.get("list") instanceof List);
		assertEquals(3, ((List<?>) parsed.get("list")).size());
		assertTrue(parsed.get("map") instanceof Map);
		assertEquals(1, ((Map<?, ?>) parsed.get("map")).size());
	}
}
