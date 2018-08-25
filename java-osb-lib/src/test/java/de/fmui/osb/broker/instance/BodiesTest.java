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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Reader;

import org.junit.Test;

import de.fmui.osb.broker.State;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.objects.Parameters;

public class BodiesTest {

	@Test
	public void testProvisionRequestBody() throws Exception {
		ProvisionRequestBody body = new ProvisionRequestBody();

		try (Reader r = JSONHelper.getJSONReader("/json/provision.json")) {
			body.load(r);
		}

		assertEquals("service-id-here", body.getServiceID());
		assertEquals("plan-id-here", body.getPlanID());
		assertEquals("org-guid-here", body.getOrganizationGUID());
		assertEquals("space-guid-here", body.getSpaceGUID());

		assertNotNull(body.getContext());
		assertEquals("cloudfoundry", body.getContext().getPlatform());
		assertEquals("some-contextual-data", body.getContext().get("some_field"));

		assertNotNull(body.getParameters());
		assertEquals(1L, body.getParameters().get("parameter1"));
		assertEquals("foo", body.getParameters().get("parameter2"));
	}

	@Test
	public void testProvisionResponseBody() throws Exception {
		ProvisionResponseBody body = new ProvisionResponseBody();

		body.setDashboardURL("http://host/something");
		assertEquals("http://host/something", body.getDashboardURL());

		body.setOperation("op1");
		assertEquals("op1", body.getOperation());

		body.validate();
	}

	@Test
	public void testDeprovisionResponseBody() throws Exception {
		DeprovisionResponseBody body = new DeprovisionResponseBody();

		body.setOperation("op1");
		assertEquals("op1", body.getOperation());

		body.validate();
	}

	@Test
	public void testUpdateServiceInstanceRequestBody() throws Exception {
		UpdateServiceInstanceRequestBody body = new UpdateServiceInstanceRequestBody();

		try (Reader r = JSONHelper.getJSONReader("/json/update.json")) {
			body.load(r);
		}

		assertEquals("service-id-here", body.getServiceID());
		assertEquals("plan-id-here", body.getPlanID());

		assertNotNull(body.getContext());
		assertEquals("cloudfoundry", body.getContext().getPlatform());
		assertEquals("some-contextual-data", body.getContext().get("some_field"));

		assertNotNull(body.getParameters());
		assertEquals(1L, body.getParameters().get("parameter1"));
		assertEquals("foo", body.getParameters().get("parameter2"));

		assertNotNull(body.getPreviousValues());
		assertEquals("service-id-here", body.getPreviousValues().getServiceID());
		assertEquals("old-plan-id-here", body.getPreviousValues().getPlanID());
		assertEquals("org-guid-here", body.getPreviousValues().getOrganizationID());
		assertEquals("space-guid-here", body.getPreviousValues().getSpaceID());

		body.validate();
	}

	@Test
	public void testFetchInstanceResponseBody() throws Exception {
		FetchInstanceResponseBody body = new FetchInstanceResponseBody();

		body.setServiceID("service1");
		assertEquals("service1", body.getServiceID());

		body.setPlanID("plan1");
		assertEquals("plan1", body.getPlanID());

		body.setDashboardURL("http://host/something");
		assertEquals("http://host/something", body.getDashboardURL());

		Parameters parameters = new Parameters();
		parameters.put("key", "value");
		body.setParameters(parameters);
		assertEquals("value", body.getParameters().get("key"));
		
		body.validate();
	}

	@Test
	public void testUpdateServiceInstanceResponseBody() throws Exception {
		UpdateServiceInstanceResponseBody body = new UpdateServiceInstanceResponseBody();

		body.setDashboardURL("http://host/something");
		assertEquals("http://host/something", body.getDashboardURL());

		body.setOperation("op1");
		assertEquals("op1", body.getOperation());

		body.validate();
	}

	@Test
	public void testInstanceLastOperationResponseBody() throws Exception {
		InstanceLastOperationResponseBody body = new InstanceLastOperationResponseBody();

		body.setState(State.IN_PROGRESS);
		assertEquals(State.IN_PROGRESS, body.getState());

		body.setDescription("desc1");
		assertEquals("desc1", body.getDescription());

		body.validate();
	}
}
