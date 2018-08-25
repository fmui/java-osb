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
package de.fmui.osb.broker.instance;

import de.fmui.osb.broker.OpenServiceBrokerResponse;

public class FetchInstanceResponse extends OpenServiceBrokerResponse {

	/**
	 * Builder class.
	 */
	public static class FetchInstanceResponseBuilder {

		private FetchInstanceResponseBody body;

		public FetchInstanceResponseBuilder() {
		}

		public FetchInstanceResponseBuilder body(FetchInstanceResponseBody body) {
			this.body = body;
			return this;
		}

		public FetchInstanceResponseBuilder ok() {
			return this;
		}

		public FetchInstanceResponse build() {
			if (body == null) {
				throw new IllegalStateException("No body set!");
			}

			FetchInstanceResponse result = new FetchInstanceResponse(this);
			return result;
		}
	}

	private FetchInstanceResponse(FetchInstanceResponseBuilder builder) {
		statusCode = 200;
		body = builder.body;
	}

	public static FetchInstanceResponseBuilder builder() {
		return new FetchInstanceResponseBuilder();
	}
}
