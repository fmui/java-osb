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

import java.util.List;
import java.util.Map;

public class OSBUtils {

	/**
	 * Compares two parameter maps.
	 * 
	 * @param params1
	 *            first parameter map
	 * @param params2
	 *            second parameter map
	 * @return {@code true} if both maps are equal, {@code false} otherwise
	 */
	public static boolean compareParameters(Map<String, Object> params1, Map<String, Object> params2) {
		return compareObjects(params1, params2);
	}

	/**
	 * Compares two objects.
	 * 
	 * @param o1
	 *            first object
	 * @param o2
	 *            second object
	 * @return {@code true} if both objects are equal, {@code false} otherwise
	 */
	private static boolean compareObjects(Object o1, Object o2) {
		if (o1 == o2 || (o1 == null && o2 == null)) {
			return true;
		}
		if ((o1 == null && o2 != null) || (o1 != null && o2 == null)) {
			return false;
		}
		if (o1 instanceof List && o2 instanceof List) {
			return compareLists((List<?>) o1, (List<?>) o2);
		}
		if (o1 instanceof Map && o2 instanceof Map) {
			return compareMaps((Map<?, ?>) o1, (Map<?, ?>) o2);
		}
		return o1.equals(o2);
	}

	private static boolean compareMaps(Map<?, ?> m1, Map<?, ?> m2) {
		if (m1.size() != m2.size()) {
			return false;
		}

		for (Object key : m1.keySet()) {
			if (!m2.containsKey(key)) {
				return false;
			}
			if (!(compareObjects(m1.get(key), m2.get(key)))) {
				return false;
			}
		}

		return true;
	}

	private static boolean compareLists(List<?> l1, List<?> l2) {
		if (l1.size() != l2.size()) {
			return false;
		}

		int n = l1.size();
		for (int i = 0; i < n; i++) {
			if (!compareObjects(l1.get(i), l2.get(i))) {
				return false;
			}
		}

		return true;
	}
}
