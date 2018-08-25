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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class RequestsTest {

	@Test
	public void testProvisionRequest() throws Exception {
		String instanceID = "123-456-78";
		String dashboardURL = "http://example-dashboard.example.com/9189kdfsk0vfnku";

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/" + instanceID,
				JSONHelper.getJSONString("/json/provision.json"));

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
			@Override
			public ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertNotNull(request.getRequestBody());
				assertEquals("service-id-here", request.getRequestBody().getServiceID());
				assertFalse(request.isAcceptsIncomplete());

				ProvisionResponseBody body = new ProvisionResponseBody();
				body.setDashboardURL(dashboardURL);

				return ProvisionResponse.builder().created().body(body).build();
			}
		});

		// check status code
		assertEquals(201, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertEquals(dashboardURL, responseBody.get("dashboard_url"));
	}

	@Test
	public void testProvisionResponses() {
		ProvisionResponseBody body = new ProvisionResponseBody();

		assertEquals(200, ProvisionResponse.builder().ok().body(body).build().getStatusCode());
		assertEquals(200, ProvisionResponse.builder().async(false).body(body).build().getStatusCode());
		assertEquals(202, ProvisionResponse.builder().accepted().body(body).build().getStatusCode());
		assertEquals(202, ProvisionResponse.builder().async(true).body(body).build().getStatusCode());
	}

	@Test
	public void testFetchInstanceRequest() throws Exception {
		String instanceID = "123-456-78";
		String dashboardURL = "http://example-dashboard.example.com/9189kdfsk0vfnku";

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("GET", "/v2/service_instances/" + instanceID);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
			@Override
			public FetchInstanceResponse fetchServiceInstance(FetchInstanceRequest request)
					throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertNull(request.getRequestBody());

				FetchInstanceResponseBody body = new FetchInstanceResponseBody();
				body.setDashboardURL(dashboardURL);

				return FetchInstanceResponse.builder().ok().body(body).build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertEquals(dashboardURL, responseBody.get("dashboard_url"));
	}

	@Test
	public void testFetchInstanceResponses() {
		FetchInstanceResponseBody body = new FetchInstanceResponseBody();

		assertEquals(200, FetchInstanceResponse.builder().ok().body(body).build().getStatusCode());
	}

	@Test
	public void testDeprovisionRequestSync() throws Exception {
		String instanceID = "123-456-78";
		String serviceID = "service-id-here";
		String planID = "plan-id-here";

		// prepare request and response object
		Map<String, String> parameters = new HashMap<>();
		parameters.put("service_id", serviceID);
		parameters.put("plan_id", planID);
		parameters.put("accepts_incomplete", "false");

		HttpServletRequest request = MockFactory.createHttpServletRequest("DELETE",
				"/v2/service_instances/" + instanceID, parameters);

		HttpServletResponse response = MockFactory.createHttpServletResponse();

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
			@Override
			public DeprovisionResponse deprovision(DeprovisionRequest request) throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertEquals(serviceID, request.getServiceID());
				assertEquals(planID, request.getPlanID());
				assertFalse(request.isAcceptsIncomplete());
				assertNull(request.getRequestBody());

				return DeprovisionResponse.builder().ok().build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testDeprovisionRequestAsync() throws Exception {
		String instanceID = "123-456-78";
		String serviceID = "service-id-here";
		String planID = "plan-id-here";
		String operation = "operation-1";

		// prepare request and response object
		Map<String, String> parameters = new HashMap<>();
		parameters.put("service_id", serviceID);
		parameters.put("plan_id", planID);
		parameters.put("accepts_incomplete", "true");

		HttpServletRequest request = MockFactory.createHttpServletRequest("DELETE",
				"/v2/service_instances/" + instanceID, parameters);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
			@Override
			public DeprovisionResponse deprovision(DeprovisionRequest request) throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertEquals(serviceID, request.getServiceID());
				assertEquals(planID, request.getPlanID());
				assertTrue(request.isAcceptsIncomplete());
				assertNull(request.getRequestBody());

				DeprovisionResponseBody body = new DeprovisionResponseBody();
				body.setOperation(operation);

				return DeprovisionResponse.builder().accepted().body(body).build();
			}
		});

		// check status code
		assertEquals(202, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertEquals(operation, responseBody.get("operation"));
	}

	@Test
	public void testDeprovisionResponses() {
		DeprovisionResponseBody body = new DeprovisionResponseBody();

		assertEquals(200, DeprovisionResponse.builder().ok().body(body).build().getStatusCode());
		assertEquals(200, DeprovisionResponse.builder().async(false).body(body).build().getStatusCode());
		assertEquals(202, DeprovisionResponse.builder().accepted().body(body).build().getStatusCode());
		assertEquals(202, DeprovisionResponse.builder().async(true).body(body).build().getStatusCode());
	}

	@Test
	public void testUpdateServiceInstanceRequest() throws Exception {
		String instanceID = "123-456-78";
		String dashboardURL = "http://example-dashboard.example.com/9189kdfsk0vfnku";

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PATCH",
				"/v2/service_instances/" + instanceID, JSONHelper.getJSONString("/json/update.json"));

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
			@Override
			public UpdateServiceInstanceResponse update(UpdateServiceInstanceRequest request)
					throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertNotNull(request.getRequestBody());
				assertEquals("service-id-here", request.getRequestBody().getServiceID());

				UpdateServiceInstanceResponseBody body = new UpdateServiceInstanceResponseBody();
				body.setDashboardURL("http://example-dashboard.example.com/9189kdfsk0vfnku");

				return UpdateServiceInstanceResponse.builder().ok().body(body).build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertEquals(dashboardURL, responseBody.get("dashboard_url"));
	}

	@Test
	public void testUpdateServiceInstanceResponses() {
		UpdateServiceInstanceResponseBody body = new UpdateServiceInstanceResponseBody();

		assertEquals(200, UpdateServiceInstanceResponse.builder().ok().body(body).build().getStatusCode());
		assertEquals(200, UpdateServiceInstanceResponse.builder().async(false).body(body).build().getStatusCode());
		assertEquals(202, UpdateServiceInstanceResponse.builder().accepted().body(body).build().getStatusCode());
		assertEquals(202, UpdateServiceInstanceResponse.builder().async(true).body(body).build().getStatusCode());
	}

	@Test
	public void testLastOperationRequest() throws Exception {
		String instanceID = "123-456-78";
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
				"/v2/service_instances/" + instanceID + "/last_operation", parameters);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {

			@Override
			public InstanceLastOperationResponse getLastOperationForInstance(InstanceLastOperationRequest request)
					throws OpenServiceBrokerException {
				assertEquals(instanceID, request.getInstanceID());
				assertEquals(serviceID, request.getServiceID());
				assertEquals(planID, request.getPlanID());
				assertEquals(operation, request.getOperation());
				assertNull(request.getRequestBody());

				InstanceLastOperationResponseBody body = new InstanceLastOperationResponseBody();
				body.setState(State.IN_PROGRESS);
				body.setDescription(description);

				return InstanceLastOperationResponse.builder().ok().body(body).build();
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
