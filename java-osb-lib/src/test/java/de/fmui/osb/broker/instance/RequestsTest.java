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
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import de.fmui.osb.broker.OpenServiceBroker;
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

				ProvisionResponseBody body = new ProvisionResponseBody();
				body.setDashboardURL("http://example-dashboard.example.com/9189kdfsk0vfnku");

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
	public void testDeprovisionRequest() throws Exception {
		String instanceID = "123-456-78";
		String serviceID = "service-id-here";
		String planID = "plan-id-here";

		// prepare request and response object
		Map<String, String> parameters = new HashMap<>();
		parameters.put("service_id", serviceID);
		parameters.put("plan_id", planID);

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
				assertNull(request.getRequestBody());

				return DeprovisionResponse.builder().ok().build();
			}
		});

		// check status code
		assertEquals(200, response.getStatus());
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
}
