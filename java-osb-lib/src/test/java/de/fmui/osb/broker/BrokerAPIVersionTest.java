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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BrokerAPIVersionTest {

	@Test
	public void testParse() throws Exception {
		BrokerAPIVersion v1 = new BrokerAPIVersion("2.14");
		assertEquals(2, v1.getMajorVersion());
		assertEquals(14, v1.getMinorVersion());

		BrokerAPIVersion v2 = new BrokerAPIVersion("  2.14  ");
		assertEquals(2, v2.getMajorVersion());
		assertEquals(14, v2.getMinorVersion());

		BrokerAPIVersion v3 = new BrokerAPIVersion("1234.5678");
		assertEquals(1234, v3.getMajorVersion());
		assertEquals(5678, v3.getMinorVersion());

		try {
			new BrokerAPIVersion(null);
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			new BrokerAPIVersion("");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			new BrokerAPIVersion("2");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			new BrokerAPIVersion("2.");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			new BrokerAPIVersion(".14");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			new BrokerAPIVersion("abc.xyz");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testCompare() throws Exception {
		BrokerAPIVersion v1 = new BrokerAPIVersion("2.14");
		BrokerAPIVersion v2 = new BrokerAPIVersion("2.14");
		assertEquals(0, v1.compareTo(v2));
		assertEquals(0, v2.compareTo(v1));
		assertTrue(v1.equals(v2));

		BrokerAPIVersion v3 = new BrokerAPIVersion("1.14");
		BrokerAPIVersion v4 = new BrokerAPIVersion("3.14");
		assertEquals(-1, v3.compareTo(v4));
		assertEquals(1, v4.compareTo(v3));
		assertFalse(v3.equals(v4));

		BrokerAPIVersion v5 = new BrokerAPIVersion("2.14");
		BrokerAPIVersion v6 = new BrokerAPIVersion("2.15");
		assertEquals(-1, v5.compareTo(v6));
		assertEquals(1, v6.compareTo(v5));
		assertFalse(v5.equals(v6));
	}
}
