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
package de.fmui.osb.broker.internal.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

public class IOUtils {
	/** UTF-8 character set name. */
	public static final String UTF8 = "UTF-8";
	/** ISO-8859-1 character set name. */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/**
	 * Copies all bytes of an input stream to an output stream.
	 * 
	 * Neither the input stream nor the output stream will the closed after the
	 * copy.
	 * 
	 * @param in
	 *            the input stream, must not be {@code null}
	 * @param out
	 *            the output stream, must not be {@code null}
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		copy(in, out, 64 * 1024);
	}

	/**
	 * Copies all bytes of an input stream to an output stream.
	 * 
	 * Neither the input stream nor the output stream will the closed after the
	 * copy.
	 * 
	 * @param in
	 *            the input stream, must not be {@code null}
	 * @param out
	 *            the output stream, must not be {@code null}
	 * @param bufferSize
	 *            the size of the internal buffer, must be positive
	 */
	public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
		int b;
		byte[] buffer = new byte[bufferSize];

		while ((b = in.read(buffer)) > -1) {
			out.write(buffer, 0, b);
		}
	}

	/**
	 * Closes a stream and ignores any exceptions.
	 * 
	 * @param closeable
	 *            the {@link Closeable} object
	 */
	public static void closeQuietly(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (final IOException ioe) {
			// ignore
		}
	}

	/**
	 * Returns UTF-8 bytes of the given string.
	 * 
	 * @param s
	 *            the input string
	 * 
	 * @return the UTF-8 bytes
	 */
	public static byte[] toUTF8Bytes(String s) {
		if (s == null) {
			return null;
		}

		try {
			return s.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding 'UTF-8'!", e);
		}
	}

	/**
	 * URL encodes the given string.
	 * 
	 * @param s
	 *            the string to encode
	 * 
	 * @return the encoded
	 */
	public static String encodeURL(String s) {
		if (s == null) {
			return null;
		}

		try {
			return URLEncoder.encode(s, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding 'UTF-8'!", e);
		}
	}

	/**
	 * URL decodes the given string.
	 * 
	 * @param s
	 *            the string to decode
	 * 
	 * @return the decoded string
	 */
	public static String decodeURL(String s) {
		if (s == null) {
			return null;
		}

		try {
			return URLDecoder.decode(s, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding 'UTF-8'!", e);
		}
	}

	/**
	 * Decodes a Base64 UTF-8 string.
	 * 
	 * @param s
	 *            Base64 encoded UTF-8 string
	 * @return the decoded string
	 */
	public static String decodeBase64UTF8String(String s) {
		if (s == null) {
			return null;
		}

		String result = null;

		byte[] bytes = Base64.getDecoder().decode(s);
		try {
			result = new String(bytes, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding 'UTF-8'!", e);
		}
		return result;
	}

	/**
	 * Decodes a Base64 ISO 8859-1 string.
	 * 
	 * @param s
	 *            Base64 encoded ISO 8859-1 string
	 * @return the decoded string
	 */
	public static String decodeBase64ISOString(String s) {
		if (s == null) {
			return null;
		}

		String result = null;

		byte[] bytes = Base64.getDecoder().decode(s);
		try {
			result = new String(bytes, ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding 'ISO-8859-1'!", e);
		}
		return result;
	}
}
