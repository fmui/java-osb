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
package de.fmui.osb.broker.binding;

import de.fmui.osb.broker.OpenServiceBrokerResponse;

public class BindingLastOperationResponse extends OpenServiceBrokerResponse {

	/**
	 * Builder class.
	 */
	public static class BindingLastOperationResponseBuilder {

		private BindingLastOperationResponseBody body;

		public BindingLastOperationResponseBuilder() {
		}

		public BindingLastOperationResponseBuilder body(BindingLastOperationResponseBody body) {
			this.body = body;
			return this;
		}

		public BindingLastOperationResponseBuilder ok() {
			return this;
		}

		public BindingLastOperationResponse build() {
			if (body == null) {
				throw new IllegalStateException("No body set!");
			}

			BindingLastOperationResponse result = new BindingLastOperationResponse(this);
			return result;
		}
	}

	private BindingLastOperationResponse(BindingLastOperationResponseBuilder builder) {
		statusCode = 200;
		body = builder.body;
	}

	public static BindingLastOperationResponseBuilder builder() {
		return new BindingLastOperationResponseBuilder();
	}
}
