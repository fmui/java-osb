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
package de.fmui.osb.broker.objects;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import de.fmui.osb.broker.exceptions.ValidationException;
import de.fmui.osb.broker.internal.json.JSONArrayImpl;
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.internal.json.parser.JSONParseException;
import de.fmui.osb.broker.internal.json.parser.JSONParser;
import de.fmui.osb.broker.json.JSONArray;
import de.fmui.osb.broker.json.JSONObject;

/**
 * Base class for all Open Service Broker objects.
 */
public abstract class AbstractOpenServiceBrokerObject extends JSONObjectImpl implements Validatable {

	private static final long serialVersionUID = 1L;

	private static final String PATTERN_CLI_NAME = "^[a-zA-Z0-9\\-\\.]*$";

	/**
	 * Creates and adds a new object.
	 */
	public JSONObject createObject(String key) {
		JSONObject result = new JSONObjectImpl();
		put(key, result);
		return result;
	}

	/**
	 * Creates and adds a new array.
	 */
	public <T> JSONArray<T> createArray(String key, Class<T> clazz) {
		return createArray(key, clazz, null);
	}

	/**
	 * Creates, fills, and adds a new array.
	 */
	public <T> JSONArray<T> createArray(String key, Class<T> clazz, T[] values) {
		JSONArray<T> result = new JSONArrayImpl<T>(clazz);
		if (values != null) {
			for (T value : values) {
				result.add(value);
			}
		}

		put(key, result);

		return result;
	}

	/**
	 * Adds elements to an array.
	 * 
	 * If the array doesn't exist, it will be created.
	 */
	public <T> JSONArray<T> addToArray(String key, Class<T> clazz, T[] values) {
		JSONArray<T> existingArray = getArray(key, clazz);
		if (existingArray == null) {
			return createArray(key, clazz, values);
		}

		if (values != null) {
			for (T value : values) {
				existingArray.add(value);
			}
		}

		return existingArray;
	}

	/**
	 * Gets and casts a value of a field.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Class is null!");
		}

		Object obj = get(key);
		if (obj == null) {
			return null;
		}

		if (clazz.isInstance(obj)) {
			return (T) obj;
		}

		throw new IllegalStateException("Invalid Type! Should be a " + clazz.getSimpleName() + "!");
	}

	/**
	 * Gets an array and check if all elements are of the given class.
	 */
	@SuppressWarnings("unchecked")
	public <T> JSONArray<T> getArray(String key, Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Class is null!");
		}

		Object obj = get(key);
		if (obj == null) {
			return null;
		}

		if (obj instanceof JSONArray) {
			for (Object element : (JSONArray<Object>) obj) {
				if (element != null && !clazz.isInstance(element)) {
					throw new IllegalStateException("Invalid Element: " + element.getClass().getSimpleName());
				}
			}

			return (JSONArray<T>) obj;
		}

		throw new IllegalStateException("Invalid Type! Should be an array!");
	}

	/**
	 * Gets and casts a value of a field to a String.
	 */
	public String getString(String key) {
		return get(key, String.class);
	}

	/**
	 * Gets and casts a value of a field to a Long.
	 */
	public Long getNumber(String key) {
		return get(key, Long.class);
	}

	/**
	 * Gets and casts a value of a field to a Boolean.
	 */
	public Boolean getBoolean(String key) {
		return get(key, Boolean.class);
	}

	public boolean isNullOrEmpty(String key) {
		Object value = get(key);
		if (value == null) {
			return true;
		}
		if (value instanceof String && ((String) value).isEmpty()) {
			return true;
		}
		if (value instanceof List && ((List<?>) value).isEmpty()) {
			return true;
		}

		return false;
	}

	public boolean isValidName(String key) {
		Object value = get(key);
		if (value == null) {
			return false;
		}

		if (!(value instanceof String)) {
			return false;
		}

		if (value.toString().isEmpty()) {
			return false;
		}

		return value.toString().matches(PATTERN_CLI_NAME);
	}

	public boolean isValidID(String key) {
		Object value = get(key);
		if (value == null) {
			return false;
		}

		if (!(value instanceof String)) {
			return false;
		}

		if (value.toString().isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public void validate() throws ValidationException {
		validateSubobjects();
	}

	protected void validateSubobjects() throws ValidationException {
		for (Object value : values()) {
			validateObject(value);
		}
	}

	private void validateObject(Object obj) throws ValidationException {
		if (obj instanceof Validatable) {
			((Validatable) obj).validate();
		} else if (obj instanceof List) {
			for (Object value : (List<?>) obj) {
				validateObject(value);
			}
		}
	}

	/**
	 * Loads a JSON into this object.
	 */
	public void load(Reader in) throws IOException {
		JSONParser parser = new JSONParser();
		try {
			parser.parse(in, this);
		} catch (JSONParseException e) {
			throw new IOException("Invalid JSON!", e);
		}
	}
}
