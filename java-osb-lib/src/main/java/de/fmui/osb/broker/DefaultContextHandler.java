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

import de.fmui.osb.broker.handler.ContextHandler;
import de.fmui.osb.broker.objects.CloudFoundryContext;
import de.fmui.osb.broker.objects.Context;
import de.fmui.osb.broker.objects.KubernetesContext;

/**
 * Default implementation of the {@link ContextHandler} interface.
 */
public class DefaultContextHandler implements ContextHandler {

	public static final String PLATFORM_CLOUD_FOUNDRY = "cloudfoundry";
	public static final String PLATFORM_KUBERNETES = "kubernetes";

	/**
	 * Converts a {@link Context} object into a {@link CloudFoundryContext} object
	 * if the platform is Cloud Foundry or a {@link KubernetesContext} object if the
	 * platform is Kubernetes or returns the original context object for any other
	 * platform.
	 */
	@Override
	public Context convertContext(Context context) {
		String platform = context.getPlatform();

		if (PLATFORM_CLOUD_FOUNDRY.equals(platform)) {
			return new CloudFoundryContext(context);
		} else if (PLATFORM_KUBERNETES.equals(platform)) {
			return new KubernetesContext(context);
		}

		return context;
	}
}
