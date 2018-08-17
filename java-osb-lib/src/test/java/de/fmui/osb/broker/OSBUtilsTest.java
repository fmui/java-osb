package de.fmui.osb.broker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class OSBUtilsTest {

	@Test
	public void testCompare() throws Exception {
		assertTrue(OSBUtils.compareParameters(Collections.singletonMap("key", "value"),
				Collections.singletonMap("key", "value")));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key", "value1"),
				Collections.singletonMap("key", "value2")));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key1", "value"),
				Collections.singletonMap("key2", "value")));

		assertTrue(OSBUtils.compareParameters(Collections.singletonMap("key", 1), Collections.singletonMap("key", 1)));

		assertTrue(OSBUtils.compareParameters(Collections.singletonMap("key", null),
				Collections.singletonMap("key", null)));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key", null),
				Collections.singletonMap("key", "something")));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key", "value1"),
				Collections.singletonMap("key", "value2")));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key1", "value"),
				Collections.singletonMap("key2", "value")));

		assertTrue(OSBUtils.compareParameters(Collections.singletonMap("key", Arrays.asList("a", "b", "c")),
				Collections.singletonMap("key", Arrays.asList("a", "b", "c"))));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key", Arrays.asList("a", "b", "c")),
				Collections.singletonMap("key", Arrays.asList("a", "b", "d"))));

		assertFalse(OSBUtils.compareParameters(Collections.singletonMap("key", Arrays.asList("a", "b", "c")),
				Collections.singletonMap("key", Arrays.asList("a", "b"))));
	}
}