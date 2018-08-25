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

import de.fmui.osb.broker.State;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.objects.Credentials;
import de.fmui.osb.broker.objects.Device;
import de.fmui.osb.broker.objects.Parameters;
import de.fmui.osb.broker.objects.VolumeMount;

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
	public void testFetchBindingResponseBody() throws Exception {
		FetchBindingResponseBody body = new FetchBindingResponseBody();

		Credentials credentials = new Credentials();
		credentials.put("cred-key", "cred-value");
		body.setCredentials(credentials);
		assertEquals("cred-value", body.getCredentials().get("cred-key"));

		VolumeMount volumeMount = new VolumeMount();
		volumeMount.setDriver("my-driver");
		volumeMount.setContainerDir("my-dir");
		volumeMount.setMode("rw");
		volumeMount.setDeviceType("my-type");
		Device device = new Device();
		device.setVolumeID("vol1");
		volumeMount.setDevice(device);
		body.addVolumeMount(volumeMount);
		assertEquals("vol1", body.getVolumeMounts().get(0).getDevice().getVolumeID());

		Parameters parameters = new Parameters();
		parameters.put("key", "value");
		body.setParameters(parameters);
		assertEquals("value", body.getParameters().get("key"));

		body.validate();
	}

	@Test
	public void testUnbindResponseBody() throws Exception {
		UnbindResponseBody body = new UnbindResponseBody();

		body.setOperation("op1");
		assertEquals("op1", body.getOperation());

		body.validate();
	}

	@Test
	public void testBindingLastOperationResponseBody() throws Exception {
		BindingLastOperationResponseBody body = new BindingLastOperationResponseBody();

		body.setState(State.IN_PROGRESS);
		assertEquals(State.IN_PROGRESS, body.getState());

		body.setDescription("desc1");
		assertEquals("desc1", body.getDescription());

		body.validate();
	}
}
