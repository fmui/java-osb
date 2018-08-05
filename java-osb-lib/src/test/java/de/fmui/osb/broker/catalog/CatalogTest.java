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
package de.fmui.osb.broker.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.Reader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import de.fmui.osb.broker.OpenServiceBroker;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.helpers.AbstractTestHandler;
import de.fmui.osb.broker.helpers.JSONHelper;
import de.fmui.osb.broker.helpers.MockFactory;
import de.fmui.osb.broker.json.JSONObject;

public class CatalogTest {

	@Test
	public void testProvisionRequest() throws Exception {

		// prepare request and response object
		HttpServletRequest request = MockFactory.createHttpServletRequest("GET", "/v2/catalog");

		CatalogResponseBody body = new CatalogResponseBody();
		try (Reader r = JSONHelper.getJSONReader("/json/catalog.json")) {
			body.load(r);
		}
		
		StringWriter stringWriter = new StringWriter();
		HttpServletResponse response = MockFactory.createHttpServletResponse(stringWriter);

		// run
		OpenServiceBroker osb = new OpenServiceBroker();
		osb.processRequest(request, response, new AbstractTestHandler() {

			@Override
			public CatalogResponse getCatalog(CatalogRequest request) throws OpenServiceBrokerException {
				assertNull(request.getRequestBody());
				return CatalogResponse.builder().ok().body(body).build();

			}
		});

		// check status code
		assertEquals(200, response.getStatus());

		// check body
		JSONObject responseBody = JSONHelper.parse(stringWriter.toString());
		assertNotNull(responseBody.get("services"));
	}

	@Test
	public void testBindRequestBody() throws Exception {
		CatalogResponseBody body = new CatalogResponseBody();

		try (Reader r = JSONHelper.getJSONReader("/json/catalog.json")) {
			body.load(r);
		}

		body.validate();
	}
}
