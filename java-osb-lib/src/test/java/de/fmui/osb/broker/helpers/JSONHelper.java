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
package de.fmui.osb.broker.helpers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import de.fmui.osb.broker.internal.io.IOUtils;
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.internal.json.parser.JSONParseException;
import de.fmui.osb.broker.internal.json.parser.JSONParser;
import de.fmui.osb.broker.json.JSONObject;

public class JSONHelper {

	public static Reader getJSONReader(String path) throws IOException {
		InputStream in = JSONHelper.class.getResourceAsStream(path);
		return new InputStreamReader(new BufferedInputStream(in), IOUtils.UTF8);
	}

	public static String getJSONString(String path) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(64 * 1024);
		try (InputStream in = JSONHelper.class.getResourceAsStream(path)) {
			IOUtils.copy(in, out);
		}
		return new String(out.toByteArray(), IOUtils.UTF8);
	}

	public static JSONObject parse(String s) throws JSONParseException {
		return new JSONParser().parse(s, new JSONObjectImpl());
	}
}
