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
import de.fmui.osb.broker.exceptions.UnauthorizedException;
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
	 * 
	 * This method should throw a {@link UnauthorizedException} if the
	 * authentication fails.
	 * 
	 * @param credentials
	 *            the credentials object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	void authenticate(RequestCredentials credentials) throws OpenServiceBrokerException;

	/**
	 * Handles a catalog request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	CatalogResponse getCatalog(CatalogRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a provision request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	ProvisionResponse provision(ProvisionRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a fetch instance request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	FetchInstanceResponse fetchServiceInstance(FetchInstanceRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an update request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	UpdateServiceInstanceResponse update(UpdateServiceInstanceRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a deprovision request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	DeprovisionResponse deprovision(DeprovisionRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an last operation request for service instances.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	InstanceLastOperationResponse getLastOperationForInstance(InstanceLastOperationRequest request)
			throws OpenServiceBrokerException;

	/**
	 * Handles a bind request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	BindResponse bind(BindRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles a fetch binding request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	FetchBindingResponse fetchServiceBinding(FetchBindingRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an unbind request.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	UnbindResponse unbind(UnbindRequest request) throws OpenServiceBrokerException;

	/**
	 * Handles an last operation request for service bindings.
	 * 
	 * @param request
	 *            the request object
	 * 
	 * @return the response object
	 * 
	 * @throws OpenServiceBrokerException
	 *             to indicate an issue or an invalid request
	 */
	BindingLastOperationResponse getLastOperationForBinding(BindingLastOperationRequest request)
			throws OpenServiceBrokerException;
}