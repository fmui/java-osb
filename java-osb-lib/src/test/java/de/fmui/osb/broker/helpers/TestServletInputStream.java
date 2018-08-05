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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import de.fmui.osb.broker.internal.io.IOUtils;

public class TestServletInputStream extends ServletInputStream {

	private ByteArrayInputStream stream;

	public TestServletInputStream(String json) {
		stream = new ByteArrayInputStream(IOUtils.toUTF8Bytes(json));
	}

	@Override
	public boolean isFinished() {
		return stream.available() == 0;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
	}

	@Override
	public int read() throws IOException {
		return stream.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return stream.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return stream.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return stream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return stream.available();
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		stream.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
	}

	@Override
	public boolean markSupported() {
		return stream.markSupported();
	}
}
