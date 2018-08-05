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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fmui.osb.broker.binding.BindRequest;
import de.fmui.osb.broker.binding.BindRequestBody;
import de.fmui.osb.broker.binding.UnbindRequest;
import de.fmui.osb.broker.catalog.CatalogRequest;
import de.fmui.osb.broker.exceptions.BadRequestException;
import de.fmui.osb.broker.exceptions.InvalidBrokerAPIVersionHeader;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.handler.ErrorLogHandler;
import de.fmui.osb.broker.handler.OpenServiceBrokerHandler;
import de.fmui.osb.broker.instance.DeprovisionRequest;
import de.fmui.osb.broker.instance.InstanceLastOperationRequest;
import de.fmui.osb.broker.instance.ProvisionRequest;
import de.fmui.osb.broker.instance.ProvisionRequestBody;
import de.fmui.osb.broker.instance.UpdateServiceInstanceRequest;
import de.fmui.osb.broker.instance.UpdateServiceInstanceRequestBody;
import de.fmui.osb.broker.internal.Constraints;
import de.fmui.osb.broker.internal.io.CappedInputStream;
import de.fmui.osb.broker.internal.io.HttpUtils;
import de.fmui.osb.broker.internal.io.IOUtils;
import de.fmui.osb.broker.internal.json.parser.JSONParseException;
import de.fmui.osb.broker.internal.json.parser.JSONParser;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.Validatable;

public class OpenServiceBroker {

	private BrokerAPIVersion brokerAPIMinVersion = new BrokerAPIVersion("2.4");

	/**
	 * Sets the minimum Broker API Version that this broker requires.
	 */
	public synchronized void setBrokerAPIMinVersion(String version) {
		brokerAPIMinVersion = new BrokerAPIVersion(version);
	}

	/**
	 * Processes an Open Service Broker request.
	 * 
	 * @param request
	 *            the HTTP request object
	 * @param response
	 *            the HTTP response object
	 * @param handler
	 *            the handler that handles the request
	 * @throws IOException
	 *             if the request cannot be read or the response cannot be sent
	 */
	public void processRequest(HttpServletRequest request, HttpServletResponse response,
			OpenServiceBrokerHandler handler) throws IOException {
		try {
			OpenServiceBrokerRequest osbRequest = null;
			OpenServiceBrokerResponse osbResponse = null;

			// check broker API version
			BrokerAPIVersion brokerAPIVersion = checkBrokerAPIVersion(request);

			// check authentication
			RequestCredentials credentials = getCredentials(request);
			handler.authenticate(credentials);

			// detect operation
			String method = request.getMethod();
			String[] path = HttpUtils.splitPath(request);

			if (isCatalogRequest(method, path)) {
				// catalog request
				osbRequest = new CatalogRequest();
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials, null);

				osbResponse = handler.getCatalog((CatalogRequest) osbRequest);
			} else if (isProvsionRequest(method, path)) {
				// provison request
				String instanceID = path[path.length - 1];

				osbRequest = new ProvisionRequest(instanceID);
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials, new ProvisionRequestBody());

				osbResponse = handler.provision((ProvisionRequest) osbRequest);
			} else if (isInstanceUpdateRequest(method, path)) {
				// update request
				String instanceID = path[path.length - 1];

				osbRequest = new UpdateServiceInstanceRequest(instanceID);
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials,
						new UpdateServiceInstanceRequestBody());

				osbResponse = handler.update((UpdateServiceInstanceRequest) osbRequest);
			} else if (isDeprovsionRequest(method, path)) {
				// deprovison request
				String instanceID = path[path.length - 1];
				String serviceID = getParameter(request, "service_id");
				String planID = getParameter(request, "plan_id");

				osbRequest = new DeprovisionRequest(instanceID, serviceID, planID);
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials, null);

				osbResponse = handler.deprovision((DeprovisionRequest) osbRequest);
			} else if (isInstanceLastOperationRequest(method, path)) {
				// instance last operation request
				String instanceID = path[path.length - 2];
				String serviceID = request.getParameter("service_id");
				String planID = request.getParameter("plan_id");
				String operation = request.getParameter("operation");

				osbRequest = new InstanceLastOperationRequest(instanceID, serviceID, planID, operation);
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials, null);

				osbResponse = handler.getLastOperationForInstance((InstanceLastOperationRequest) osbRequest);
			} else if (isBindRequest(method, path)) {
				// instance last operation request
				String instanceID = path[path.length - 3];
				String bindingID = path[path.length - 1];

				osbRequest = new BindRequest(instanceID, bindingID);
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials, new BindRequestBody());

				osbResponse = handler.bind((BindRequest) osbRequest);
			} else if (isUnbindRequest(method, path)) {
				// instance last operation request
				String instanceID = path[path.length - 3];
				String bindingID = path[path.length - 1];
				String serviceID = getParameter(request, "service_id");
				String planID = getParameter(request, "plan_id");

				osbRequest = new UnbindRequest(instanceID, bindingID, serviceID, planID);
				populateRequestObject(osbRequest, request, brokerAPIVersion, credentials, null);

				osbResponse = handler.unbind((UnbindRequest) osbRequest);
			}

			if (osbRequest == null) {
				// no pattern matched -> no OSB request
				throw new OpenServiceBrokerException(400, "InvalidPath", "Not a OSB v2 path.");
			}

			if (osbResponse == null || osbResponse.getStatusCode() < 200 || osbResponse.getStatusCode() > 299) {
				// handler did not return a valid response
				if (handler instanceof ErrorLogHandler) {
					((ErrorLogHandler) handler).logError("No OSB response.");
				}
				throw new OpenServiceBrokerException(500, "InternalError", "Processing error");
			}

			HttpUtils.sendResponse(response, osbResponse);
		} catch (OpenServiceBrokerException osbe) {
			HttpUtils.sendError(response, osbe);
		} catch (ValidationException ve) {
			HttpUtils.sendError(response, new BadRequestException(ve.toString(), ve));
		} catch (Exception e) {
			// handle all other exceptions and return a generic error
			if (handler instanceof ErrorLogHandler) {
				((ErrorLogHandler) handler).logError("Could not process OSB request: " + e.getMessage(), e);
			}

			OpenServiceBrokerException error = new OpenServiceBrokerException(500, "InternalError", "Processing error",
					e);
			HttpUtils.sendError(response, error);
		}
	}

	protected boolean isCatalogRequest(String method, String[] path) {
		return "GET".equals(method) && //
				path.length >= 2 && //
				"v2".equals(path[path.length - 2]) && //
				"catalog".equals(path[path.length - 1]);
	}

	protected boolean isProvsionRequest(String method, String[] path) {
		return "PUT".equals(method) && //
				path.length >= 3 && //
				"v2".equals(path[path.length - 3]) && //
				"service_instances".equals(path[path.length - 2]);
	}

	protected boolean isDeprovsionRequest(String method, String[] path) {
		return "DELETE".equals(method) && //
				path.length >= 3 && //
				"v2".equals(path[path.length - 3]) && //
				"service_instances".equals(path[path.length - 2]);
	}

	protected boolean isInstanceFetchRequest(String method, String[] path) {
		return "GET".equals(method) && //
				path.length >= 3 && //
				"v2".equals(path[path.length - 3]) && //
				"service_instances".equals(path[path.length - 2]);
	}

	protected boolean isInstanceUpdateRequest(String method, String[] path) {
		return "PATCH".equals(method) && //
				path.length >= 3 && //
				"v2".equals(path[path.length - 3]) && //
				"service_instances".equals(path[path.length - 2]);
	}

	protected boolean isInstanceLastOperationRequest(String method, String[] path) {
		return "GET".equals(method) && //
				path.length >= 4 && //
				"v2".equals(path[path.length - 4]) && //
				"service_instances".equals(path[path.length - 3]) && //
				"last_operation".equals(path[path.length - 1]);
	}

	protected boolean isBindRequest(String method, String[] path) {
		return "PUT".equals(method) && //
				path.length >= 5 && //
				"v2".equals(path[path.length - 5]) && //
				"service_instances".equals(path[path.length - 4]) && //
				"service_bindings".equals(path[path.length - 2]);
	}

	protected boolean isUnbindRequest(String method, String[] path) {
		return "DELETE".equals(method) && //
				path.length >= 5 && //
				"v2".equals(path[path.length - 5]) && //
				"service_instances".equals(path[path.length - 4]) && //
				"service_bindings".equals(path[path.length - 2]);
	}

	protected boolean isFetchRequest(String method, String[] path) {
		return "GET".equals(method) && //
				path.length >= 5 && //
				"v2".equals(path[path.length - 5]) && //
				"service_instances".equals(path[path.length - 4]) && //
				"service_bindings".equals(path[path.length - 2]);
	}

	protected boolean isBindingLastOperationRequest(String method, String[] path) {
		return "GET".equals(method) && //
				path.length >= 6 && //
				"v2".equals(path[path.length - 6]) && //
				"service_instances".equals(path[path.length - 5]) && //
				"service_bindings".equals(path[path.length - 3]) && //
				"last_operation".equals(path[path.length - 1]);
	}

	protected BrokerAPIVersion checkBrokerAPIVersion(HttpServletRequest request) throws OpenServiceBrokerException {
		String brokerAPIVersionHeader = request.getHeader("X-Broker-API-Version");
		if (brokerAPIVersionHeader == null) {
			throw new InvalidBrokerAPIVersionHeader("X-Broker-API-Version header is missing!");
		}

		BrokerAPIVersion brokerAPIVersion = null;
		try {
			brokerAPIVersion = new BrokerAPIVersion(brokerAPIVersionHeader);
		} catch (Exception e) {
			throw new InvalidBrokerAPIVersionHeader("X-Broker-API-Version header is invalid!", e);
		}

		if (brokerAPIVersion.getMajorVersion() != 2) {
			throw new InvalidBrokerAPIVersionHeader("This broker only supports OSB v2!");
		}

		if (brokerAPIVersion.compareTo(brokerAPIMinVersion) == -1) {
			throw new InvalidBrokerAPIVersionHeader(
					"This broker requires OSB version " + brokerAPIMinVersion.toString() + " or later!");
		}

		return brokerAPIVersion;
	}

	protected RequestCredentials getCredentials(HttpServletRequest request) throws OpenServiceBrokerException {
		RequestCredentials credentials;

		credentials = BasicAuthCredentials.createCredentialsFromRequest(request);
		if (credentials != null) {
			return credentials;
		}

		credentials = BearerCredentials.createCredentialsFromRequest(request);
		if (credentials != null) {
			return credentials;
		}

		return null;
	}

	protected boolean getAcceptsIncomplete(HttpServletRequest request) {
		return "true".equals(request.getParameter("accepts_incomplete"));
	}

	protected String getParameter(HttpServletRequest request, String name) throws OpenServiceBrokerException {
		String s = request.getParameter(name);
		if (s == null || s.isEmpty()) {
			throw new BadRequestException("Query parameter '" + name + "' is missing!");
		}

		return s;
	}

	protected OriginatingIdentity getOriginatingIdentity(HttpServletRequest request) throws OpenServiceBrokerException {
		String originatingIdentityHeader = request.getHeader("X-Broker-API-Originating-Identity");
		if (originatingIdentityHeader != null) {
			try {
				return new OriginatingIdentity(originatingIdentityHeader);
			} catch (Exception e) {
				throw new BadRequestException("X-Broker-API-Originating-Identity header is invalid!", e);
			}
		}

		return null;
	}

	protected <T> T parseBody(HttpServletRequest request, T root)
			throws OpenServiceBrokerException, ValidationException {
		if (request.getMethod().equals("PUT") || request.getMethod().equals("PATCH")) {
			// check content type
			String contentType = request.getContentType();
			if (contentType == null || !contentType.equalsIgnoreCase("application/json")) {
				throw new BadRequestException("Content type is not set or not 'applictaion/json'!");
			}

			// parse
			Reader in = null;
			JSONParser parser = new JSONParser();
			try {
				in = new InputStreamReader(
						new CappedInputStream(new BufferedInputStream(request.getInputStream(), 64 * 1024),
								Constraints.MAX_BODY),
						IOUtils.UTF8);
				parser.parse(in, root);
			} catch (JSONParseException jpe) {
				throw new BadRequestException("Invalid JSON object!", jpe);
			} catch (IOException ioe) {
				throw new BadRequestException("IO Error!", ioe);
			} finally {
				IOUtils.closeQuietly(in);
			}

			if (root instanceof Validatable) {
				((Validatable) root).validate();
			}

			return root;
		}

		return null;
	}

	protected void populateRequestObject(OpenServiceBrokerRequest osbRequest, HttpServletRequest request,
			BrokerAPIVersion brokerAPIVersion, RequestCredentials credentials, JSONObject root)
			throws OpenServiceBrokerException, ValidationException {
		osbRequest.setBrokerAPIVersion(brokerAPIVersion);
		osbRequest.setCredentials(credentials);
		osbRequest.setAcceptsIncomplete(getAcceptsIncomplete(request));
		osbRequest.setOriginatingIdentity(getOriginatingIdentity(request));
		osbRequest.setHttpServletRequest(request);
		if (root != null) {
			osbRequest.setRequestBody(parseBody(request, root));
		}
	}
}
