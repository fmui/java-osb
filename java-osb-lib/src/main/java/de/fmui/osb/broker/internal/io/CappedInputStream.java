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

import java.io.IOException;
import java.io.InputStream;

/**
 * A stream that counts bytes and throws an exception if the given maximum is
 * reached. Counted bytes can be deducted to excludes parts of stream from the
 * length limitation.
 */
public class CappedInputStream extends InputStream {

	private InputStream stream;
	private long max;
	private long counter;

	public CappedInputStream(InputStream stream, long max) {
		this.stream = stream;
		this.max = max;
		this.counter = 0;
	}

	/**
	 * Returns the counter.
	 */
	public long getCounter() {
		return counter;
	}

	private void checkLength() throws IOException {
		if (counter > max) {
			throw new IOException("Input limit exceeded!");
		}
	}

	@Override
	public int available() throws IOException {
		return stream.available();
	}

	@Override
	public synchronized void mark(int readlimit) {
	}

	@Override
	public synchronized void reset() throws IOException {
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read() throws IOException {
		checkLength();

		int b = stream.read();
		if (b > -1) {
			counter++;
		}

		return b;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		checkLength();

		int l = stream.read(b, off, len);
		counter += l;

		return l;
	}

	@Override
	public int read(byte[] b) throws IOException {
		checkLength();

		int l = stream.read(b);
		counter += l;

		return l;
	}

	@Override
	public long skip(long n) throws IOException {
		checkLength();

		long l = stream.skip(n);
		counter += l;

		return l;
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}
}
