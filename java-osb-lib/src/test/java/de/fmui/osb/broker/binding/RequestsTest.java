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
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.helpers.AbstractTestHandler;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.helpers.MockFactory;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.Credentials;

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
}
