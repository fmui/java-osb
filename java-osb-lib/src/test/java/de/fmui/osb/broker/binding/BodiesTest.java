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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Reader;

import org.junit.Test;

import de.fmui.osb.broker.helpers.JSONHelper;

public class BodiesTest {

	@Test
	public void tesBindRequestBody() throws Exception {
		BindRequestBody body = new BindRequestBody();

		try (Reader r = JSONHelper.getJSONReader("/json/binding.json")) {
			body.load(r);
		}

		assertEquals("service-id-here", body.getServiceID());
		assertEquals("plan-id-here", body.getPlanID());

		assertNotNull(body.getContext());
		assertEquals("cloudfoundry", body.getContext().getPlatform());
		assertEquals("some-contextual-data", body.getContext().get("some_field"));

		assertNotNull(body.getBindResource());
		assertEquals("app-guid-here", body.getBindResource().getAppGUID());

		assertNotNull(body.getParameters());
		assertEquals(1L, body.getParameters().get("parameter1-name-here"));
		assertEquals("parameter2-value-here", body.getParameters().get("parameter2-name-here"));
	}

	@Test
	public void testUnbindResponseBody() throws Exception {
		UnbindResponseBody body = new UnbindResponseBody();

		body.setOperation("op1");
		assertEquals("op1", body.getOperation());

		body.validate();
	}

}
