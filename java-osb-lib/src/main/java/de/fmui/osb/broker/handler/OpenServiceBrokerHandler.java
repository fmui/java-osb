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
package de.fmui.osb.broker.handler;

import de.fmui.osb.broker.RequestCredentials;
import de.fmui.osb.broker.binding.BindRequest;
import de.fmui.osb.broker.binding.BindResponse;
import de.fmui.osb.broker.binding.BindingLastOperationRequest;
import de.fmui.osb.broker.binding.BindingLastOperationResponse;
import de.fmui.osb.broker.binding.FetchBindingRequest;
import de.fmui.osb.broker.binding.FetchBindingResponse;
import de.fmui.osb.broker.binding.UnbindRequest;
import de.fmui.osb.broker.binding.UnbindResponse;
import de.fmui.osb.broker.catalog.CatalogRequest;
import de.fmui.osb.broker.catalog.CatalogResponse;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.instance.DeprovisionRequest;
import de.fmui.osb.broker.instance.DeprovisionResponse;
import de.fmui.osb.broker.instance.FetchInstanceRequest;
import de.fmui.osb.broker.instance.FetchInstanceResponse;
import de.fmui.osb.broker.instance.InstanceLastOperationRequest;
import de.fmui.osb.broker.instance.InstanceLastOperationResponse;
import de.fmui.osb.broker.instance.ProvisionRequest;
import de.fmui.osb.broker.instance.ProvisionResponse;
import de.fmui.osb.broker.instance.UpdateServiceInstanceRequest;
import de.fmui.osb.broker.instance.UpdateServiceInstanceResponse;

/**
 * Handler interface for Open Service Broker requests.
 */
public interface OpenServiceBrokerHandler {

	/**
	 * Handles authentication.
	 */
	void authenticate(RequestCredentials credentials) throws OpenServiceBrokerException;

	/**
	 * Handles a catalog request.
	 */
	CatalogResponse getCatalog(CatalogRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a provision request.
	 */
	ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a fetch instance request.
	 */
	FetchInstanceResponse fetchServiceInstance(FetchInstanceRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an update request.
	 */
	UpdateServiceInstanceResponse update(UpdateServiceInstanceRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a deprovision request.
	 */
	DeprovisionResponse deprovision(DeprovisionRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an last operation request for service instances.
	 */
	InstanceLastOperationResponse getLastOperationForInstance(InstanceLastOperationRequest request)
			throws OpenServiceBrokerException;

	/**
	 * Handles a bind request.
	 */
	BindResponse bind(BindRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a fetch binding request.
	 */
	FetchBindingResponse fetchServiceBinding(FetchBindingRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an unbind request.
	 */
	UnbindResponse unbind(UnbindRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an last operation request for service bindings.
	 */
	BindingLastOperationResponse getLastOperationForBinding(BindingLastOperationRequest request)
			throws OpenServiceBrokerException;

}
