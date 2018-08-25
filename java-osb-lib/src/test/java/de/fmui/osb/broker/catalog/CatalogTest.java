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

import static de.fmui.osb.broker.helpers.ValidationHelper.assertNotValidating;
import static de.fmui.osb.broker.helpers.ValidationHelper.assertValidating;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import de.fmui.osb.broker.objects.Plan;
import de.fmui.osb.broker.objects.PlanMetadata;
import de.fmui.osb.broker.objects.SchemaParameters;
import de.fmui.osb.broker.objects.Service;
import de.fmui.osb.broker.objects.ServiceMetadata;

public class CatalogTest {

	@Test
	public void testCatalogRequest() throws Exception {

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
	public void testCatalogResponseBody() throws Exception {
		CatalogResponseBody body = new CatalogResponseBody();

		try (Reader r = JSONHelper.getJSONReader("/json/catalog.json")) {
			body.load(r);
		}

		body.validate();

		Service service1 = body.getService("e1270a8f-50a6-476d-9e16-c456c0815949");
		assertNotNull(service1);
		assertEquals("e1270a8f-50a6-476d-9e16-c456c0815949", service1.getID());
		assertEquals("fake-service", service1.getName());
		assertNotNull(service1.getDescription());

		Service service2 = body.getService("none");
		assertNull(service2);

		Plan plan1 = body.getPlan("e1270a8f-50a6-476d-9e16-c456c0815949", "cebcb73b-da3a-404d-bf6c-f37087600fe1");
		assertNotNull(plan1);
		assertEquals("cebcb73b-da3a-404d-bf6c-f37087600fe1", plan1.getID());
		assertEquals("fake-plan-1", plan1.getName());
		assertNotNull(plan1.getDescription());

		Plan plan2 = body.getPlan("e1270a8f-50a6-476d-9e16-c456c0815949", "none");
		assertNull(plan2);

		Plan plan3 = body.getPlan("none", "cebcb73b-da3a-404d-bf6c-f37087600fe1");
		assertNull(plan3);
	}

	@Test
	public void testServiceObject() throws Exception {
		Service service = new Service();

		assertTrue(service.isNullOrEmpty(Service.KEY_ID));

		assertNotValidating(service);

		service.setID("id-1");
		service.setName("name-1");
		service.setDescription("desc-1");
		service.setBindable(true);

		Plan plan = new Plan();
		plan.setID("id-1");
		plan.setName("name-1");
		plan.setDescription("desc-1");
		service.addPlan(plan);

		assertValidating(service);

		service.setTags("tag-1", "tag-2", "tag-3");
		assertEquals(3, service.getTags().size());

		service.addTag("tag-4", "tag-5");
		assertEquals(5, service.getTags().size());

		service.setRequires(Service.REQUIRES_ROUTE_FORWARDING, Service.REQUIRES_SYSLOG_DRAIN);
		assertEquals(2, service.getRequires().size());

		service.addRequires(Service.REQUIRES_VOLUME_MOUNT);
		assertEquals(3, service.getRequires().size());

		ServiceMetadata metadata = new ServiceMetadata();
		metadata.setDisplayName("my-name");
		metadata.put("some-key", "some-value");
		service.setMetadata(metadata);
		assertEquals("my-name", metadata.getDisplayName());
		assertEquals("some-value", metadata.get("some-key"));
	}

	@Test
	public void testServiceName() throws Exception {
		Service service = new Service();

		assertFalse(service.isValidName(Service.KEY_NAME));

		service.setName(null);
		assertFalse(service.isValidName(Service.KEY_NAME));

		service.setName("");
		assertFalse(service.isValidName(Service.KEY_NAME));

		service.setName("test name");
		assertFalse(service.isValidName(Service.KEY_NAME));

		service.setName("test name");
		assertFalse(service.isValidName(Service.KEY_NAME));

		service.setName("test/name");
		assertFalse(service.isValidName(Service.KEY_NAME));

		service.setName("my-name");
		assertTrue(service.isValidName(Service.KEY_NAME));

		service.setName("my.name");
		assertTrue(service.isValidName(Service.KEY_NAME));

		service.setName("1-name");
		assertTrue(service.isValidName(Service.KEY_NAME));

		service.setName("name567");
		assertTrue(service.isValidName(Service.KEY_NAME));

		service.setName("name");
		assertTrue(service.isValidName(Service.KEY_NAME));
	}

	@Test
	public void testPlanObject() throws Exception {
		Plan plan = new Plan();

		assertNotValidating(plan);

		plan.setID("id-1");
		plan.setName("name-1");
		plan.setDescription("desc-1");

		assertValidating(plan);

		PlanMetadata metadata = new PlanMetadata();
		metadata.setDisplayName("my-name");
		metadata.setBullets("a", "b", "c");
		plan.setMetadata(metadata);
		assertEquals("my-name", plan.getMetadata().getDisplayName());
		assertTrue(plan.getMetadata().getBullets().contains("b"));
	}

	@Test
	public void testSchemaObjects() throws Exception {
		Plan plan = new Plan();

		// instance create
		SchemaParameters instanceCreateSchema = new SchemaParameters();
		instanceCreateSchema.setDefaults();

		JSONObject prop1 = instanceCreateSchema.addProperty("prop1");
		prop1.put("type", "string");
		prop1.put("description", "Some string.");

		plan.setServiceInstanceCreateSchema(instanceCreateSchema);

		assertEquals(prop1.get("type"), ((JSONObject) plan.getSchemas().getServiceInstanceSchema().getCreateSchema()
				.getParameters().getProperties().get("prop1")).get("type"));

		// instance update
		SchemaParameters instanceUpdateSchema = new SchemaParameters();
		instanceUpdateSchema.setDefaults();

		JSONObject prop2 = instanceUpdateSchema.addProperty("prop2");
		prop2.put("type", "integer");
		prop2.put("description", "A number.");

		plan.setServiceInstanceUpdateSchema(instanceUpdateSchema);

		assertEquals(prop2.get("type"), ((JSONObject) plan.getSchemas().getServiceInstanceSchema().getUpdateSchema()
				.getParameters().getProperties().get("prop2")).get("type"));

		// binding create
		SchemaParameters bindingCreateSchema = new SchemaParameters();
		bindingCreateSchema.setDefaults();

		JSONObject prop3 = bindingCreateSchema.addProperty("prop3");
		prop3.put("type", "object");
		prop3.put("description", "A thing.");

		plan.setServiceBindingCreateSchema(bindingCreateSchema);

		assertEquals(prop3.get("type"), ((JSONObject) plan.getSchemas().getServiceBindingSchema().getCreateSchema()
				.getParameters().getProperties().get("prop3")).get("type"));
	}
}
