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

public class InstanceLastOperationResponse extends OpenServiceBrokerResponse {

	/**
	 * Builder class.
	 */
	public static class InstanceLastOperationResponseBuilder {

		private InstanceLastOperationResponseBody body;

		public InstanceLastOperationResponseBuilder() {
		}

		public InstanceLastOperationResponseBuilder body(InstanceLastOperationResponseBody body) {
			this.body = body;
			return this;
		}

		public InstanceLastOperationResponseBuilder ok() {
			return this;
		}

		public InstanceLastOperationResponse build() {
			if (body == null) {
				throw new IllegalStateException("No body set!");
			}

			InstanceLastOperationResponse result = new InstanceLastOperationResponse(this);
			return result;
		}
	}

	private InstanceLastOperationResponse(InstanceLastOperationResponseBuilder builder) {
		statusCode = 200;
		body = builder.body;
	}

	public static InstanceLastOperationResponseBuilder builder() {
		return new InstanceLastOperationResponseBuilder();
	}
}
