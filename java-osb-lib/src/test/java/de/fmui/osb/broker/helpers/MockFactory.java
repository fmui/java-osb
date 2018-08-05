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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class MockFactory {

	public static HttpServletRequest createHttpServletRequest(String method, String path) throws Exception {
		return createHttpServletRequest(method, path, null, null);
	}

	public static HttpServletRequest createHttpServletRequest(String method, String path, String body)
			throws Exception {
		return createHttpServletRequest(method, path, body, null);
	}

	public static HttpServletRequest createHttpServletRequest(String method, String path,
			Map<String, String> paramaters) throws Exception {
		return createHttpServletRequest(method, path, null, paramaters);
	}

	public static HttpServletRequest createHttpServletRequest(String method, String path, String body,
			Map<String, String> paramaters) throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getMethod()).thenReturn(method);

		when(request.getContextPath()).thenReturn("/context");
		when(request.getServletPath()).thenReturn("/servlet");
		when(request.getRequestURI()).thenReturn("/context/servlet" + path);

		if (paramaters != null) {
			paramaters.forEach((key, value) -> {
				when(request.getParameter(key)).thenReturn(value);
			});
		}

		when(request.getHeader("Authorization")).thenReturn("basic dXNlcjpwYXNz");
		when(request.getHeader("X-Broker-API-Originating-Identity")).thenReturn(
				"someplatform eyANCiAgInVzZXJfaWQiOiAiNjgzZWE3NDgtMzA5Mi00ZmY0LWI2NTYtMzljYWNjNGQ1MzYwIiwNCiAgInVzZXJfbmFtZSI6ICJqb2VAZXhhbXBsZS5jb20iDQp9");
		when(request.getHeader("X-Broker-API-Version")).thenReturn("2.14");

		if (body != null) {
			when(request.getContentType()).thenReturn("application/json");

			ServletInputStream servletInputStream = new TestServletInputStream(body);
			when(request.getInputStream()).thenReturn(servletInputStream);
		}

		return request;
	}

	public static HttpServletResponse createHttpServletResponse() throws Exception {
		return createHttpServletResponse(null);
	}

	public static HttpServletResponse createHttpServletResponse(StringWriter stringWriter) throws Exception {

		if (stringWriter == null) {
			stringWriter = new StringWriter();
		}
		PrintWriter writer = new PrintWriter(stringWriter);

		HttpServletResponse response = new HttpServletResponseWrapper(mock(HttpServletResponse.class)) {
			private int sc;

			@Override
			public PrintWriter getWriter() throws IOException {
				return writer;
			}

			@Override
			public void setStatus(int sc) {
				this.sc = sc;
			}

			@Override
			public int getStatus() {
				return sc;
			}
		};

		return response;
	}
}
