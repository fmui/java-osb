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
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import de.fmui.osb.broker.OpenServiceBroker;
import de.fmui.osb.broker.State;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.helpers.AbstractTestHandler;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.helpers.MockFactory;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.Credentials;
import de.fmui.osb.broker.objects.Parameters;

public class RequestsTest {

	@Test
	public void testBindRequest() throws Exception {
		String instanceID = "123-456-78";
		String bindingID = "abc-xyz";

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT",
				"/v2/service_instances/" + instanceID + "/service_bindings/" + bindingID,
				JSONHelper.getJSONString("/json/binding.json"));

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {

			@Override
			public BindResponse bind(BindRequest request) throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertNotNull(request.getRequestBody());

				BindResponseBody body = new BindResponseBody();
				body.setCredentials(new Credentials());

				return BindResponse.builder().created().body(body).build();
			}
		});

		// check status code
		assertEquals(201, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("credentials"));
	}

	@Test
	public void testBindResponses() {
		BindResponseBody body = new BindResponseBody();

		assertEquals(200, BindResponse.builder().ok().body(body).build().getStatusCode());
		assertEquals(200, BindResponse.builder().async(false).body(body).build().getStatusCode());
		assertEquals(202, BindResponse.builder().accepted().body(body).build().getStatusCode());
		assertEquals(202, BindResponse.builder().async(true).body(body).build().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFetchBindingRequest() throws Exception {
		String instanceID = "123-456-78";
		String bindingID = "abc-xyz";

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("GET",
				"/v2/service_instances/" + instanceID + "/service_bindings/" + bindingID);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {

			@Override
			public FetchBindingResponse fetchServiceBinding(FetchBindingRequest request)
					throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertEquals(bindingID, request.getBindingID());
				assertNull(request.getRequestBody());

				FetchBindingResponseBody body = new FetchBindingResponseBody();
				Parameters parameters = new Parameters();
				parameters.put("key", "value");
				body.setParameters(parameters);

				return FetchBindingResponse.builder().ok().body(body).build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertEquals("value", ((Map<String, Object>) responseBody.get("parameters")).get("key"));
	}

	@Test
	public void testFetchBindingResponses() {
		FetchBindingResponseBody body = new FetchBindingResponseBody();

		assertEquals(200, FetchBindingResponse.builder().ok().body(body).build().getStatusCode());
	}

	@Test
	public void testUnbindRequest() throws Exception {
		String instanceID = "123-456-78";
		String bindingID = "abc-xyz";
		String serviceID = "service-id-here";
		String planID = "plan-id-here";

		// prepare request and response object
		Map<String, String> parameters = new HashMap<>();
		parameters.put("service_id", serviceID);
		parameters.put("plan_id", planID);

		HttpServletRequest request = MockFactory.createHttpServletRequest("DELETE",
				"/v2/service_instances/" + instanceID + "/service_bindings/" + bindingID, parameters);

		HttpServletResponse response = MockFactory.createHttpServletResponse();

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {

			@Override
			public UnbindResponse unbind(UnbindRequest request) throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertEquals(bindingID, request.getBindingID());
				assertEquals(serviceID, request.getServiceID());
				assertEquals(planID, request.getPlanID());
				assertNull(request.getRequestBody());

				return UnbindResponse.builder().ok().build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testUnbindResponses() {
		UnbindResponseBody body = new UnbindResponseBody();

		assertEquals(200, UnbindResponse.builder().ok().body(body).build().getStatusCode());
		assertEquals(200, UnbindResponse.builder().async(false).body(body).build().getStatusCode());
		assertEquals(202, UnbindResponse.builder().accepted().body(body).build().getStatusCode());
		assertEquals(202, UnbindResponse.builder().async(true).body(body).build().getStatusCode());
	}

	@Test
	public void testLastOperationRequest() throws Exception {
		String instanceID = "123-456-78";
		String bindingID = "abc-def";
		String serviceID = "service-id-here";
		String planID = "plan-id-here";
		String operation = "operation-42";
		String description = "Still working on it.";

		// prepare request and response object
		Map<String, String> parameters = new HashMap<>();
		parameters.put("service_id", serviceID);
		parameters.put("plan_id", planID);
		parameters.put("operation", operation);

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("GET",
				"/v2/service_instances/" + instanceID + "/service_bindings/" + bindingID + "/last_operation",
				parameters);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {

			@Override
			public BindingLastOperationResponse getLastOperationForBinding(BindingLastOperationRequest request)
					throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertEquals(bindingID, request.getBindingID());
				assertEquals(serviceID, request.getServiceID());
				assertEquals(planID, request.getPlanID());
				assertEquals(operation, request.getOperation());
				assertNull(request.getRequestBody());

				BindingLastOperationResponseBody body = new BindingLastOperationResponseBody();
				body.setState(State.IN_PROGRESS);
				body.setDescription(description);

				return BindingLastOperationResponse.builder().ok().body(body).build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertEquals(State.IN_PROGRESS, State.fromValue((String) responseBody.get("state")));
		assertEquals(description, responseBody.get("description"));
	}
}
