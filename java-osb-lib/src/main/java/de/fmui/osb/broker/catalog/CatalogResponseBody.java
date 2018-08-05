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

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.json.JSONArray;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.AbstractOpenServiceBrokerObject;
import de.fmui.osb.broker.objects.KeyMapping;
import de.fmui.osb.broker.objects.Plan;
import de.fmui.osb.broker.objects.Service;

@KeyMapping(jsonKey = "services", osbClass = Service.class)
public class CatalogResponseBody extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_SERVICES = "services";

	public CatalogResponseBody() {
	}

	public JSONArray<Service> getServices() {
		return getArray(KEY_SERVICES, Service.class);
	}

	public void setServices(Service... services) {
		createArray(KEY_SERVICES, Service.class, services);
	}

	public void addService(Service... services) {
		addToArray(KEY_SERVICES, Service.class, services);
	}

	public Service getService(String serviceID) {
		if (serviceID == null) {
			throw new IllegalArgumentException("Service ID is not set!");
		}

		JSONArray<Service> services = getServices();
		if (services == null) {
			return null;
		}

		for (Service service : services) {
			if (serviceID.equals(service.getID())) {
				return service;
			}
		}

		return null;
	}

	public Plan getPlan(String serviceID, String planID) {
		if (serviceID == null) {
			throw new IllegalArgumentException("Service ID is not set!");
		}
		if (planID == null) {
			throw new IllegalArgumentException("Plan ID is not set!");
		}

		JSONArray<Service> services = getServices();
		if (services == null) {
			return null;
		}

		for (Service service : services) {
			if (serviceID.equals(service.getID())) {
				JSONArray<Plan> plans = service.getPlans();
				if (plans != null) {
					for (Plan plan : plans) {
						if (planID.equals(plan.getID())) {
							return plan;
						}
					}
				}

				return null;
			}
		}

		return null;
	}

	@Override
	public void validate() throws ValidationException {
		if (isNullOrEmpty(KEY_SERVICES)) {
			throw new ValidationException("No services provided!");
		}

		super.validate();
	}
}
