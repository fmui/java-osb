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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fmui.osb.broker.objects.CloudFoundryContext;
import de.fmui.osb.broker.objects.Context;
import de.fmui.osb.broker.objects.KubernetesContext;

public class ContextHandlerTest {

	@Test
	public void testCloudFoundryContext() throws Exception {
		String orgGUID = "123";
		String spaceGUID = "abc";

		Context context = new Context();
		context.put(Context.KEY_PLATFORM, "cloudfoundry");
		context.put("organization_guid", orgGUID);
		context.put("space_guid", spaceGUID);

		DefaultContextHandler handler = new DefaultContextHandler();
		Context newContext = handler.convertContext(context);

		assertTrue(newContext instanceof CloudFoundryContext);

		CloudFoundryContext cloudFoundryContext = (CloudFoundryContext) newContext;
		assertEquals(orgGUID, cloudFoundryContext.getOrganizationGUID());
		assertEquals(spaceGUID, cloudFoundryContext.getSpaceGUID());
	}

	@Test
	public void testKubernetesContext() throws Exception {
		String clusterid = "123";
		String namespace = "abc";

		Context context = new Context();
		context.put(Context.KEY_PLATFORM, "kubernetes");
		context.put("clusterid", clusterid);
		context.put("namespace", namespace);

		DefaultContextHandler handler = new DefaultContextHandler();
		Context newContext = handler.convertContext(context);

		assertTrue(newContext instanceof KubernetesContext);

		KubernetesContext kubernetesContext = (KubernetesContext) newContext;
		assertEquals(clusterid, kubernetesContext.getClusterID());
		assertEquals(namespace, kubernetesContext.getNamespace());
	}
}
