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

public class UnbindResponse extends OpenServiceBrokerResponse {

	/**
	 * Builder class.
	 */
	public static class UnbindResponseBuilder {

		private int statusCode = 0;
		private UnbindResponseBody body;

		public UnbindResponseBuilder() {
		}

		public UnbindResponseBuilder body(UnbindResponseBody body) {
			this.body = body;
			return this;
		}

		public UnbindResponseBuilder ok() {
			statusCode = 200;
			body = new UnbindResponseBody();
			return this;
		}

		public UnbindResponseBuilder accepted() {
			statusCode = 202;
			return this;
		}

		public UnbindResponse build() {
			if (statusCode != 200 && statusCode != 202) {
				throw new IllegalStateException("No response code set!");
			}

			if (body == null) {
				throw new IllegalStateException("No body set!");
			}

			UnbindResponse result = new UnbindResponse(this);
			return result;
		}
	}

	private UnbindResponse(UnbindResponseBuilder builder) {
		statusCode = builder.statusCode;
		body = builder.body;
	}

	public static UnbindResponseBuilder builder() {
		return new UnbindResponseBuilder();
	}
}
