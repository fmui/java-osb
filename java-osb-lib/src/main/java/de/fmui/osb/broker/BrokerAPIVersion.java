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

/**
 * Open Service Broker API Version.
 */
public class BrokerAPIVersion implements Comparable<BrokerAPIVersion> {

	private int major;
	private int minor;

	/**
	 * Constructor.
	 * 
	 * @param version
	 *            the version as string
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided string is not a version string
	 */
	public BrokerAPIVersion(String version) {
		parse(version);
	}

	/**
	 * Returns the major version.
	 * 
	 * @return the major version
	 */
	public int getMajorVersion() {
		return major;
	}

	/**
	 * Returns the minor version.
	 * 
	 * @return the minor version
	 */
	public int getMinorVersion() {
		return minor;
	}

	private void parse(String version) {
		if (version == null) {
			throw new IllegalArgumentException("Invalid Broker-API-Version!");
		}
		String[] parts = version.trim().split("\\.");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid Broker-API-Version!");
		}

		try {
			major = Integer.parseInt(parts[0]);
			minor = Integer.parseInt(parts[1]);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Invalid Broker-API-Version!", nfe);
		}
	}

	@Override
	public int compareTo(BrokerAPIVersion o) {
		if (major == o.major) {
			if (minor == o.minor) {
				return 0;
			} else {
				return minor > o.minor ? 1 : -1;
			}
		} else {
			return major > o.major ? 1 : -1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		BrokerAPIVersion other = (BrokerAPIVersion) obj;
		if (major != other.major || minor != other.minor) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return major + "." + minor;
	}
}
