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
package de.fmui.osb.broker.example;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fmui.osb.broker.OpenServiceBroker;
import de.fmui.osb.broker.example.fake.FakeService;
import de.fmui.osb.broker.example.handler.AsyncBrokerExampleHandler;
import de.fmui.osb.broker.example.handler.SyncBrokerExampleHandler;

@WebServlet("/broker/*")
public class BrokerExampleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private SyncBrokerExampleHandler syncHandler;
	private AsyncBrokerExampleHandler asyncHandler;
	private OpenServiceBroker broker;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			syncHandler = new SyncBrokerExampleHandler(new FakeService(0));
			asyncHandler = new AsyncBrokerExampleHandler(new FakeService(60));
			broker = new OpenServiceBroker();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPathInfo().startsWith("/sync/")) {
			// http://<host>/<context>/broker/sync/...
			// -> synchronous broker handler example
			broker.processRequest(request, response, syncHandler);
		} else if (request.getPathInfo().startsWith("/async/")) {
			// http://<host>/<context>/broker/async/...
			// -> asynchronous broker handler example
			broker.processRequest(request, response, asyncHandler);
		} else {
			// none of the above -> NOT FOUND
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

	}
}
