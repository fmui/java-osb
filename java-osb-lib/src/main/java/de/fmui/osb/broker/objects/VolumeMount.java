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

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.json.JSONObject;

@KeyMapping(jsonKey = "device", osbClass = Device.class)
public class VolumeMount extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_DRIVER = "driver";
	public static final String KEY_CONTAINER_DIR = "container_dir";
	public static final String KEY_MODE = "mode";
	public static final String KEY_DEVICE_TYPE = "device_type";
	public static final String KEY_DEVICE = "device";

	public String getDriver() {
		return getString(KEY_DRIVER);
	}

	public void setDriver(String driver) {
		put(KEY_DRIVER, driver);
	}

	public String getContainerDir() {
		return getString(KEY_CONTAINER_DIR);
	}

	public void setContainerDir(String containerDir) {
		put(KEY_CONTAINER_DIR, containerDir);
	}

	public String getMode() {
		return getString(KEY_MODE);
	}

	public void setMode(String mode) {
		put(KEY_MODE, mode);
	}

	public String getDeviceType() {
		return getString(KEY_DEVICE_TYPE);
	}

	public void setDeviceType(String deviceType) {
		put(KEY_DEVICE_TYPE, deviceType);
	}

	public Device getDevice() {
		return get(KEY_DEVICE, Device.class);
	}

	public void setDevice(Device device) {
		put(KEY_DEVICE, device);
	}

	@Override
	public void validate() throws ValidationException {
		if (isNullOrEmpty(KEY_DRIVER)) {
			throw new ValidationException("Invalid or missing driver!");
		}
		if (isNullOrEmpty(KEY_CONTAINER_DIR)) {
			throw new ValidationException("Invalid or missing container dir!");
		}
		if (isNullOrEmpty(KEY_MODE)) {
			throw new ValidationException("Invalid or missing mode!");
		}
		if (isNullOrEmpty(KEY_DEVICE_TYPE)) {
			throw new ValidationException("Invalid or missing device type!");
		}
		if (isNullOrEmpty(KEY_DEVICE)) {
			throw new ValidationException("Invalid or missing device!");
		}

		super.validate();
	}
}
