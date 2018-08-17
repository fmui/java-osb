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
package de.fmui.osb.broker.helpers;

import de.fmui.osb.broker.RequestCredentials;
import de.fmui.osb.broker.binding.BindRequest;
import de.fmui.osb.broker.binding.BindResponse;
import de.fmui.osb.broker.binding.UnbindRequest;
import de.fmui.osb.broker.binding.UnbindResponse;
import de.fmui.osb.broker.catalog.CatalogRequest;
import de.fmui.osb.broker.catalog.CatalogResponse;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.handler.ErrorLogHandler;
import de.fmui.osb.broker.handler.OpenServiceBrokerHandler;
import de.fmui.osb.broker.instance.DeprovisionRequest;
import de.fmui.osb.broker.instance.DeprovisionResponse;
import de.fmui.osb.broker.instance.InstanceLastOperationRequest;
import de.fmui.osb.broker.instance.InstanceLastOperationResponse;
import de.fmui.osb.broker.instance.ProvisionRequest;
import de.fmui.osb.broker.instance.ProvisionResponse;
import de.fmui.osb.broker.instance.UpdateServiceInstanceRequest;
import de.fmui.osb.broker.instance.UpdateServiceInstanceResponse;

public abstract class AbstractTestHandler implements OpenServiceBrokerHandler, ErrorLogHandler {

	@Override
	public void authenticate(RequestCredentials credentials) throws OpenServiceBrokerException {
	}

	@Override
	public CatalogResponse getCatalog(CatalogRequest request) throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public DeprovisionResponse deprovision(DeprovisionRequest request) throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public InstanceLastOperationResponse getLastOperationForInstance(InstanceLastOperationRequest request)
			throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public UpdateServiceInstanceResponse update(UpdateServiceInstanceRequest request)
			throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public BindResponse bind(BindRequest request) throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public UnbindResponse unbind(UnbindRequest request) throws OpenServiceBrokerException {
		return null;
	}

	@Override
	public void logError(String message, Object... args) {
		System.err.printf(message, args);
		if (args != null) {
			for (Object arg : args) {
				if (arg instanceof Throwable) {
					System.err.println();
					((Throwable) arg).printStackTrace(System.err);
				}
			}
		}
	}
}
