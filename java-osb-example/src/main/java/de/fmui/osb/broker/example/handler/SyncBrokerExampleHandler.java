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
package de.fmui.osb.broker.example.handler;

import java.io.IOException;
import java.util.UUID;

import de.fmui.osb.broker.OSBUtils;
import de.fmui.osb.broker.RequestCredentials;
import de.fmui.osb.broker.State;
import de.fmui.osb.broker.binding.BindRequest;
import de.fmui.osb.broker.binding.BindResponse;
import de.fmui.osb.broker.binding.BindResponseBody;
import de.fmui.osb.broker.binding.UnbindRequest;
import de.fmui.osb.broker.binding.UnbindResponse;
import de.fmui.osb.broker.catalog.CatalogRequest;
import de.fmui.osb.broker.catalog.CatalogResponse;
import de.fmui.osb.broker.catalog.CatalogResponseBody;
import de.fmui.osb.broker.example.fake.FakeService;
import de.fmui.osb.broker.example.fake.FakeServiceBinding;
import de.fmui.osb.broker.example.fake.FakeServiceInstance;
import de.fmui.osb.broker.exceptions.BadRequestException;
import de.fmui.osb.broker.exceptions.ConflictException;
import de.fmui.osb.broker.exceptions.GoneException;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.handler.OpenServiceBrokerHandler;
import de.fmui.osb.broker.instance.DeprovisionRequest;
import de.fmui.osb.broker.instance.DeprovisionResponse;
import de.fmui.osb.broker.instance.InstanceLastOperationRequest;
import de.fmui.osb.broker.instance.InstanceLastOperationResponse;
import de.fmui.osb.broker.instance.InstanceLastOperationResponseBody;
import de.fmui.osb.broker.instance.ProvisionRequest;
import de.fmui.osb.broker.instance.ProvisionResponse;
import de.fmui.osb.broker.instance.ProvisionResponseBody;
import de.fmui.osb.broker.instance.UpdateServiceInstanceRequest;
import de.fmui.osb.broker.instance.UpdateServiceInstanceResponse;
import de.fmui.osb.broker.instance.UpdateServiceInstanceResponseBody;
import de.fmui.osb.broker.objects.Credentials;

public class SyncBrokerExampleHandler implements OpenServiceBrokerHandler {

	private FakeService fakeService;
	private CatalogResponseBody catalog;

	public SyncBrokerExampleHandler(FakeService fakeService) throws IOException {
		this.fakeService = fakeService;

		// load catalog from file into memory
		catalog = BrokerUtils.readCatalogFromResourceFile("/catalog.json");
	}

	@Override
	public void authenticate(RequestCredentials credentials) throws OpenServiceBrokerException {
		BrokerUtils.authenticate(credentials, "username", "password");
	}

	@Override
	public CatalogResponse getCatalog(CatalogRequest request) throws OpenServiceBrokerException {
		// return the catalog from memory
		return CatalogResponse.builder().body(catalog).build();
	}

	@Override
	public ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException {
		// check service and plan
		if (catalog.getPlan(request.getRequestBody().getServiceID(), request.getRequestBody().getPlanID()) == null) {
			throw new BadRequestException("Unknown service or plan!");
		}

		// check if there is already an instance with this ID
		FakeServiceInstance existingInstance = fakeService.getServiceInstance(request.getInstanceID());
		String dashboardURL = "https://fake.example.com/instance/" + request.getInstanceID();

		if (existingInstance == null) {
			// there is no instance with this ID -> create one
			FakeServiceInstance instance = new FakeServiceInstance(request);
			fakeService.addServiceInstance(instance);

			// send response
			ProvisionResponseBody body = new ProvisionResponseBody();
			body.setDashboardURL(dashboardURL);

			return ProvisionResponse.builder().created().body(body).build();
		} else {
			// there is already an instance with this ID
			if (OSBUtils.compareParameters(request.getRequestBody().getParameters(),
					existingInstance.getParameters())) {
				// the platform provided the same parameters
				ProvisionResponseBody body = new ProvisionResponseBody();
				body.setDashboardURL(dashboardURL);

				return ProvisionResponse.builder().ok().body(body).build();
			} else {
				// the platform provided different parameters -> this is a conflict
				throw new ConflictException("Instance already exists but with different parameters!");
			}
		}
	}

	@Override
	public UpdateServiceInstanceResponse update(UpdateServiceInstanceRequest request)
			throws OpenServiceBrokerException {
		// check service and plan
		if (catalog.getPlan(request.getRequestBody().getServiceID(), request.getRequestBody().getPlanID()) == null) {
			throw new BadRequestException("Unknown service or plan!");
		}

		// check if there is an instance with this ID
		FakeServiceInstance existingInstance = fakeService.getServiceInstance(request.getInstanceID());
		if (existingInstance == null) {
			throw new BadRequestException("Unknown instance.");
		}

		// update the instance
		existingInstance.update(request);

		// send empty response
		UpdateServiceInstanceResponseBody body = new UpdateServiceInstanceResponseBody();

		return UpdateServiceInstanceResponse.builder().ok().body(body).build();
	}

	@Override
	public DeprovisionResponse deprovision(DeprovisionRequest request) throws OpenServiceBrokerException {
		// remove instance
		boolean removed = fakeService.removeServiceInstance(request.getInstanceID());

		if (removed) {
			// instance existed and is now gone
			return DeprovisionResponse.builder().ok().build();
		} else {
			// instance was unknown
			throw new GoneException("Unknown instance.");
		}
	}

	@Override
	public InstanceLastOperationResponse getLastOperationForInstance(InstanceLastOperationRequest request)
			throws OpenServiceBrokerException {
		FakeServiceInstance instance = fakeService.getServiceInstance(request.getInstanceID());
		if (instance == null) {
			// there is no such instance
			throw new BadRequestException("Unknown instance.");
		} else {
			// the state must be "succeeded" if the instance exists, because this broker
			// works synchronously
			InstanceLastOperationResponseBody body = new InstanceLastOperationResponseBody();
			body.setState(State.SUCCEEDED);

			return InstanceLastOperationResponse.builder().ok().body(body).build();
		}
	}

	@Override
	public BindResponse bind(BindRequest request) throws OpenServiceBrokerException {
		FakeServiceInstance instance = fakeService.getServiceInstance(request.getInstanceID());
		if (instance == null) {
			// there is no such instance
			throw new GoneException("Unknown instance.");
		}

		FakeServiceBinding extsingBinding = instance.getServiceBinding(request.getBindingID());
		if (extsingBinding == null) {
			// there is no binding with this ID -> create one
			FakeServiceBinding binding = new FakeServiceBinding(request);

			// create some fake credentials
			Credentials credentials = new Credentials();
			credentials.put("fake_user", UUID.randomUUID().toString());
			credentials.put("fake_password", UUID.randomUUID().toString());
			credentials.put("fake_url", "https://fake.example.com/instance/" + request.getInstanceID() + "/binding/"
					+ request.getBindingID());
			binding.setCredentials(credentials);

			// add the binding to the instance
			instance.addServiceBinding(binding);

			// send the response
			BindResponseBody body = new BindResponseBody();
			body.setCredentials(credentials);

			return BindResponse.builder().created().body(body).build();
		} else {
			// there is already an instance with this ID
			if (OSBUtils.compareParameters(request.getRequestBody().getParameters(), extsingBinding.getParameters())) {
				// the platform provided the same parameters
				BindResponseBody body = new BindResponseBody();
				body.setCredentials(extsingBinding.getCredentials());

				return BindResponse.builder().ok().body(body).build();
			} else {
				// the platform provided different parameters -> this is a conflict
				throw new ConflictException("Binding already exists but with different parameters!");
			}
		}
	}

	@Override
	public UnbindResponse unbind(UnbindRequest request) throws OpenServiceBrokerException {
		FakeServiceInstance instance = fakeService.getServiceInstance(request.getInstanceID());
		if (instance == null) {
			// there is no such instance
			throw new GoneException("Unknown instance.");
		}

		boolean removed = instance.removeServiceBinding(request.getBindingID());

		if (removed) {
			// binding existed and is now gone
			return UnbindResponse.builder().ok().build();
		} else {
			// binding was unknown
			throw new GoneException("Unknown binding.");
		}
	}

}