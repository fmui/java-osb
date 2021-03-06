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
package de.fmui.osb.broker.objects;

import de.fmui.osb.broker.json.JSONObject;

@KeyMapping(jsonKey = "service_instance", osbClass = ServiceInstanceSchema.class)
@KeyMapping(jsonKey = "service_binding", osbClass = ServiceBindingSchema.class)
public class Schemas extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public final static String KEY_SERVICE_INSTANCE = "service_instance";
	public final static String KEY_SERVICE_BINDING = "service_binding";

	public ServiceInstanceSchema getServiceInstanceSchema() {
		return get(KEY_SERVICE_INSTANCE, ServiceInstanceSchema.class);
	}

	public void setServiceInstanceSchema(ServiceInstanceSchema schema) {
		put(KEY_SERVICE_INSTANCE, schema);
	}

	public ServiceBindingSchema getServiceBindingSchema() {
		return get(KEY_SERVICE_BINDING, ServiceBindingSchema.class);
	}

	public void setServiceBindingSchema(ServiceBindingSchema schema) {
		put(KEY_SERVICE_BINDING, schema);
	}
}
