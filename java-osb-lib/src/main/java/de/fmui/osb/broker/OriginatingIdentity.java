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
package de.fmui.osb.broker;

import de.fmui.osb.broker.internal.io.IOUtils;
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.internal.json.parser.JSONParser;
import de.fmui.osb.broker.json.JSONObject;

/**
 * Originating Identity.
 */
public class OriginatingIdentity {

	private String header;
	private String platform;
	private String value;
	private JSONObject json;

	public OriginatingIdentity(String header) {
		this.header = header;
		parse(header);
	}

	public String getHeader() {
		return header;
	}

	public String getPlatform() {
		return platform;
	}

	public String getValue() {
		return value;
	}

	public JSONObject getValueAsJSON() {
		return json;
	}

	private void parse(String header) {
		String[] parts = header.split(" ");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid X-Broker-API-Originating-Identity header!");
		}

		platform = parts[0].trim();

		try {
			value = IOUtils.decodeBase64UTF8String(parts[1].trim());
			JSONParser parser = new JSONParser();
			json = parser.parse(value, new JSONObjectImpl());
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid X-Broker-API-Originating-Identity header!", e);
		}
	}
}
