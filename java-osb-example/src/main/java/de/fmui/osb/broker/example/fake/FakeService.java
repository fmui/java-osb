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
package de.fmui.osb.broker.example.fake;

import java.util.HashMap;
import java.util.Map;

public class FakeService {

	private Map<String, FakeServiceInstance> instances = new HashMap<>();
	private int delay;

	public FakeService(int delay) {
		this.delay = delay;
	}

	public synchronized void addServiceInstance(FakeServiceInstance instance) {
		instances.put(instance.getId(), instance);
	}

	public synchronized FakeServiceInstance getServiceInstance(String instanceID) {
		FakeServiceInstance instance = instances.get(instanceID);
		if (instance != null) {
			if (instance.isGone()) {
				instances.remove(instanceID);
				return null;
			}
		}

		return instance;
	}

	public synchronized boolean removeServiceInstance(String instanceID) {
		return instances.remove(instanceID) != null;
	}

	public int getDelay() {
		return delay;
	}
}
