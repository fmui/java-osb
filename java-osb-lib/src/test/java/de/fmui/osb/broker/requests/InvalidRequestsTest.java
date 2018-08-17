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
package de.fmui.osb.broker.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import de.fmui.osb.broker.OpenServiceBroker;
import de.fmui.osb.broker.helpers.AbstractTestHandler;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.helpers.MockFactory;
import de.fmui.osb.broker.json.JSONObject;

public class InvalidRequestsTest {

	@Test
	public void testInvalidMethodsOrPaths() throws Exception {
		executeRequest("GET", "/v2/catalog/test");
		executeRequest("PUT", "/v2/catalog");
		executeRequest("PUT", "/v2/service_instances/");
		executeRequest("DELETE", "/v2/service_instances/123");
		executeRequest("GET", " /v2/service_instances//last_operation");
	}

	private HttpServletResponse executeRequest(String method, String path) throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest(method, path);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(400, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));

		return response;
	}

	@Test
	public void testInvalidBody() throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/12345", "bla");

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(400, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));
	}

	@Test
	public void testInvalidJSONBody() throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/12345", "{}");

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(400, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));
	}

	@Test
	public void testBrokerAPIVersion2x() throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/12345", "bla");

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.setBrokerAPIMinVersion("2.9999");
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(412, response.getStatus());

		// check body
		JSONObject responseBody1 = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody1.get("error"));
	}

	@Test
	public void testBrokerAPIVersion3x() throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/12345", "bla");
		when(request.getHeader("X-Broker-API-Version")).thenReturn("3.9999");

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		OpenServiceBroker osb = new OpenServiceBroker();

		// run
		osb.setBrokerAPIMinVersion("2.12");
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(412, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));
	}

	@Test
	public void testBrokerAPIVersionInvalid() throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/12345", "bla");
		when(request.getHeader("X-Broker-API-Version")).thenReturn("abc");

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		OpenServiceBroker osb = new OpenServiceBroker();

		// run
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(412, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));
	}

	@Test
	public void testBrokerAPIVersionMissing() throws Exception {
		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("PUT", "/v2/service_instances/12345", "bla");
		when(request.getHeader("X-Broker-API-Version")).thenReturn(null);

		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		OpenServiceBroker osb = new OpenServiceBroker();

		// run
		osb.processRequest(request, response, new AbstractTestHandler() {
		});

		// check status code
		assertEquals(412, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("error"));
	}
}
