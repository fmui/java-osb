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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fmui.osb.broker.handler.ContextHandler;
import de.fmui.osb.broker.handler.ErrorLogHandler;
import de.fmui.osb.broker.handler.OpenServiceBrokerHandler;

/**
 * Base servlet for a stand-alone Service Broker.
 * 
 * 
 * The stub of a final Service Broker servlet looks like this:
 * 
 * <pre>
 * <code>
 * {@literal @}WebServlet("/my-broker/*")
 * public class MyBrokerServlet extends OpenServiceBrokerServlet {
 *   {@literal @}Override
 *   public void init(ServletConfig config) throws ServletException {
 *     setOpenServiceBrokerHandler(new MyOSBHandler());
 *   }
 * }
 * </code>
 * </pre>
 */
public class OpenServiceBrokerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_BROKER_HANDLER_CLASS = "brokerHandlerClass";
	public static final String PARAM_ERROR_LOG_HANDLER_CLASS = "errLogHandlerClass";
	public static final String PARAM_CONTEXT_HANDLER_CLASS = "contextHandlerClass";
	public static final String PARAM_MIN_BROKER_API_VERSION = "minBrokerAPIVersion";

	private OpenServiceBrokerHandler handler;
	private final OpenServiceBroker broker = new OpenServiceBroker();

	public OpenServiceBrokerServlet() {
		super();
	}

	public OpenServiceBrokerServlet(OpenServiceBrokerHandler handler) {
		this();
		this.handler = handler;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// get broker handler
		String brokerHandlerClassName = config.getInitParameter(PARAM_BROKER_HANDLER_CLASS);
		if (brokerHandlerClassName == null || brokerHandlerClassName.isEmpty()) {
			if (handler == null) {
				throw new ServletException("No Open Service Broker handler configured!");
			}
		} else {
			handler = createInstance(brokerHandlerClassName, OpenServiceBrokerHandler.class);
		}

		// get error handler
		String errorLogHandlerClassName = config.getInitParameter(PARAM_ERROR_LOG_HANDLER_CLASS);
		if (errorLogHandlerClassName != null) {
			broker.setErrorLogHandler(createInstance(errorLogHandlerClassName, ErrorLogHandler.class));
		}

		// get context handler
		String contextHandlerClassName = config.getInitParameter(PARAM_CONTEXT_HANDLER_CLASS);
		if (contextHandlerClassName != null) {
			broker.setContextHandler(createInstance(contextHandlerClassName, ContextHandler.class));
		}

		// get min broker API Version
		String minBrokerAPIVersion = config.getInitParameter(PARAM_MIN_BROKER_API_VERSION);
		if (minBrokerAPIVersion != null) {
			broker.setBrokerAPIMinVersion(minBrokerAPIVersion);
		}
	}

	protected OpenServiceBroker getOpenServiceBroker() {
		return broker;
	}

	protected OpenServiceBrokerHandler getOpenServiceBrokerHandler() {
		return handler;
	}

	protected void setOpenServiceBrokerHandler(OpenServiceBrokerHandler handler) {
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	protected <T> T createInstance(String classname, Class<T> superclass) throws ServletException {
		// get class
		Class<?> clazz = null;
		try {
			clazz = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			throw new ServletException("Class not found: " + classname, e);
		}

		if (!superclass.isAssignableFrom(clazz)) {
			throw new ServletException(
					"Class not found: '" + classname + "' is not assignable to '" + superclass.getName() + "'.");
		}

		// create instance
		try {
			return (T) clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new ServletException("Instance cannot be created: " + classname, e);
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		getOpenServiceBroker().processRequest(request, response, getOpenServiceBrokerHandler());
	}
}
