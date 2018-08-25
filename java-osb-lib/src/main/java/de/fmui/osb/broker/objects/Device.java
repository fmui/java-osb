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

public class Device extends AbstractOpenServiceBrokerObject implements JSONObject {

	private static final long serialVersionUID = 1L;

	public static final String KEY_VOLUME_ID = "volume_id";
	public static final String KEY_MOUNT_CONFIG = "mount_config";

	public String getVolumeID() {
		return getString(KEY_VOLUME_ID);
	}

	public void setVolumeID(String volumeID) {
		put(KEY_VOLUME_ID, volumeID);
	}

	public JSONObject getMountConfig() {
		return getJSONObject(KEY_MOUNT_CONFIG);
	}

	public void setMountConfig(JSONObject config) {
		put(KEY_MOUNT_CONFIG, config);
	}

	@Override
	public void validate() throws ValidationException {
		if (isNullOrEmpty(KEY_VOLUME_ID)) {
			throw new ValidationException("Invalid or missing volume ID!");
		}

		super.validate();
	}
}
