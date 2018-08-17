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
package de.fmui.osb.broker.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import de.fmui.osb.broker.OpenServiceBroker;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.handler.OpenServiceBrokerHandler;
import de.fmui.osb.broker.helpers.AbstractTestHandler;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.helpers.MockFactory;
import de.fmui.osb.broker.instance.ProvisionRequest;
import de.fmui.osb.broker.instance.ProvisionResponse;
import de.fmui.osb.broker.json.JSONObject;

public class InvalidResponsesTest {

	@Test
	public void testException() throws Exception {
		executeRequest(new AbstractTestHandler() {
			@Override
			public ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException {
				throw new RuntimeException("This is a test!");
			}
		});
	}

	@Test
	public void testNullResponse() throws Exception {
		executeRequest(new AbstractTestHandler() {
			@Override
			public ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException {
				return null;
			}
		});
	}

	private void executeRequest(OpenServiceBrokerHandler handler) throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/abc",
				JSONHelper.getJSONString("/json/provision.json"));

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, handler);

		// check status code
		assertEquals(500, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));
	}
}
