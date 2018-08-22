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

public class ProvisionResponse extends OpenServiceBrokerResponse {

	/**
	 * Builder class.
	 */
	public static class ProvisionResponseBuilder {

		private int statusCode = 0;
		private ProvisionResponseBody body;

		public ProvisionResponseBuilder() {
		}

		public ProvisionResponseBuilder body(ProvisionResponseBody body) {
			this.body = body;
			return this;
		}

		public ProvisionResponseBuilder async(boolean async) {
			return async ? accepted() : ok();
		}
		
		public ProvisionResponseBuilder ok() {
			statusCode = 200;
			return this;
		}

		public ProvisionResponseBuilder created() {
			statusCode = 201;
			return this;
		}

		public ProvisionResponseBuilder accepted() {
			statusCode = 202;
			return this;
		}

		public ProvisionResponse build() {
			if (statusCode != 200 && statusCode != 201 && statusCode != 202) {
				throw new IllegalStateException("No response code set!");
			}

			if (body == null) {
				throw new IllegalStateException("No body set!");
			}

			ProvisionResponse result = new ProvisionResponse(this);
			return result;
		}
	}

	private ProvisionResponse(ProvisionResponseBuilder builder) {
		statusCode = builder.statusCode;
		body = builder.body;
	}

	public static ProvisionResponseBuilder builder() {
		return new ProvisionResponseBuilder();
	}
}
