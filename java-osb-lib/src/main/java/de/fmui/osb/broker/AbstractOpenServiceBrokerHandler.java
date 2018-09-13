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

import java.io.IOException;
import java.io.InputStreamReader;

import de.fmui.osb.broker.binding.BindingLastOperationRequest;
import de.fmui.osb.broker.binding.BindingLastOperationResponse;
import de.fmui.osb.broker.binding.FetchBindingRequest;
import de.fmui.osb.broker.binding.FetchBindingResponse;
import de.fmui.osb.broker.catalog.CatalogResponseBody;
import de.fmui.osb.broker.exceptions.BadRequestException;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.handler.OpenServiceBrokerHandler;
import de.fmui.osb.broker.instance.FetchInstanceRequest;
import de.fmui.osb.broker.instance.FetchInstanceResponse;
import de.fmui.osb.broker.instance.InstanceLastOperationRequest;
import de.fmui.osb.broker.instance.InstanceLastOperationResponse;
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.internal.json.parser.JSONParseException;
import de.fmui.osb.broker.internal.json.parser.JSONParser;
import de.fmui.osb.broker.json.JSONObject;

public abstract class AbstractOpenServiceBrokerHandler implements OpenServiceBrokerHandler {

	@Override
	public FetchInstanceResponse fetchServiceInstance(FetchInstanceRequest request) throws OpenServiceBrokerException {
		throw new BadRequestException("Fetching a Service Binding is not supported!");
	}

	@Override
	public InstanceLastOperationResponse getLastOperationForInstance(InstanceLastOperationRequest request)
			throws OpenServiceBrokerException {
		throw new BadRequestException("Asynchronous instance operations is not supported!");
	}

	@Override
	public FetchBindingResponse fetchServiceBinding(FetchBindingRequest request) throws OpenServiceBrokerException {
		throw new BadRequestException("Fetching a Service Binding is not supported!");
	}

	@Override
	public BindingLastOperationResponse getLastOperationForBinding(BindingLastOperationRequest request)
			throws OpenServiceBrokerException {
		throw new BadRequestException("Asynchronous binding operations is not supported!");
	}

	// --- helper methods ---

	/**
	 * Reads the catalog from a resource file.
	 * 
	 * @param path
	 *            the resource path
	 * 
	 * @return a ready-to-use {@link CatalogResponseBody} object
	 * 
	 * @throws IOException
	 *             if reading or parsing the catalog file fails
	 */
	public CatalogResponseBody readCatalogFromResourceFile(String path) throws IOException {
		CatalogResponseBody result = new CatalogResponseBody();

		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(this.getClass().getResourceAsStream(path), "UTF-8");
			result.load(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return result;
	}

	/**
	 * Reads a JSON object from a resource file.
	 * 
	 * @param path
	 *            the resource path
	 * 
	 * @return the JSON object
	 * 
	 * @throws IOException
	 *             if reading or parsing the catalog file fails
	 */
	public JSONObject readJSONFromResourceFile(String path) throws IOException {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(this.getClass().getResourceAsStream(path), "UTF-8");
			JSONParser parser = new JSONParser();
			return parser.parse(reader, new JSONObjectImpl());
		} catch (JSONParseException e) {
			throw new IOException("Invalid JSON!", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
