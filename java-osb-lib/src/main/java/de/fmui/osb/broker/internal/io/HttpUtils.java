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
package de.fmui.osb.broker.internal.io;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fmui.osb.broker.OpenServiceBrokerResponse;
import de.fmui.osb.broker.exceptions.OpenServiceBrokerException;
import de.fmui.osb.broker.json.JSONObject;

public class HttpUtils {

	/**
	 * Sends a JSON response.
	 */
	public static void sendJSON(final HttpServletResponse response, int statusCode, String json) throws IOException {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		response.setCharacterEncoding(IOUtils.UTF8);
		PrintWriter pw = response.getWriter();
		pw.println(json);
		pw.flush();
	}

	/**
	 * Sends an OSB error response.
	 */
	public static void sendError(final HttpServletResponse response, OpenServiceBrokerException exception)
			throws IOException {
		if (!response.isCommitted()) {
			HttpUtils.sendJSON(response, exception.getStatusCode(), exception.toJSONString());
		}
	}

	/**
	 * Sends an OSB response.
	 */
	public static void sendResponse(final HttpServletResponse response, OpenServiceBrokerResponse osbResponse)
			throws IOException {
		response.setStatus(osbResponse.getStatusCode());

		JSONObject body = osbResponse.getResponseBody();
		if (body != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding(IOUtils.UTF8);
			PrintWriter pw = response.getWriter();
			body.writeJSONString(pw);
			pw.flush();
		}
	}

	/**
	 * Splits the path into its fragments.
	 */
	public static String[] splitPath(final HttpServletRequest request) {
		assert request != null;

		int prefixLength = request.getContextPath().length() + request.getServletPath().length();
		String p = request.getRequestURI().substring(prefixLength);

		if (p.length() == 0) {
			return new String[0];
		}

		String[] result = p.substring(1).split("/");
		for (int i = 0; i < result.length; i++) {
			result[i] = IOUtils.decodeURL(result[i]);

			// check for malicious characters
			for (int j = 0; j < result[i].length(); j++) {
				char c = result[i].charAt(j);
				if (c == '\n' || c == '\r' || c == '\b' || c == 0) {
					throw new RuntimeException("Invalid path!");
				}
			}
		}

		return result;
	}

	/**
	 * Splits the "Authorization" header.
	 */
	public static String[] splitAuthHeader(final HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String[] ah = authHeader.split(" ");
			if (ah.length > 1) {
				return ah;
			}
		}

		return null;
	}
}
