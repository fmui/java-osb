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
package de.fmui.osb.broker.internal.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import de.fmui.osb.broker.json.JSONArray;

/**
 * A JSON array. JSONObject supports java.util.List interface.
 */
public class JSONArrayImpl<T> extends ArrayList<T> implements JSONArray<T> {
	private static final long serialVersionUID = 1L;

	private final Class<T> clazz;

	public JSONArrayImpl(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	public Class<T> getArrayClass() {
		return clazz;
	}

	public JSONArrayImpl<T> createSubArray() {
		return new JSONArrayImpl<T>(clazz);
	}

	/**
	 * Encode a list into JSON text and write it to out. If this list is also a
	 * JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific
	 * behaviours will be ignored at this top level.
	 * 
	 * @param list
	 * @param out
	 */
	public static void writeJSONString(List<?> list, Writer out) throws IOException {
		if (list == null) {
			out.write("null");
			return;
		}

		boolean first = true;

		out.write('[');
		for (Object value : list) {
			if (first) {
				first = false;
			} else {
				out.write(',');
			}

			if (value == null) {
				out.write("null");
				continue;
			}

			JSONValue.writeJSONString(value, out);
		}
		out.write(']');
	}

	public void writeJSONString(Writer out) throws IOException {
		writeJSONString(this, out);
	}

	/**
	 * Convert a list to JSON text. The result is a JSON array. If this list is also
	 * a JSONAware, JSONAware specific behaviours will be omitted at this top level.
	 * 
	 * @param list
	 * @return JSON text, or "null" if list is null.
	 */
	public static String toJSONString(List<?> list) {
		if (list == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder(1024);
		addJSONString(list, sb);
		return sb.toString();
	}

	public static void addJSONString(List<?> list, StringBuilder sb) {
		if (list == null) {
			sb.append("null");
			return;
		}

		boolean first = true;

		sb.append('[');
		for (Object value : list) {
			if (first) {
				first = false;
			} else {
				sb.append(',');
			}

			if (value == null) {
				sb.append("null");
				continue;
			}
			sb.append(JSONValue.toJSONString(value));
		}
		sb.append(']');
	}

	public String toJSONString() {
		return toJSONString(this);
	}

	public String toString() {
		return toJSONString();
	}
}
