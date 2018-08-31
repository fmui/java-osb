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
import java.util.Map;

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
	 * Default constructor.
	 */
	public AbstractOpenServiceBrokerObject() {
		super();
	}

	/**
	 * Constructor with initial values.
	 * 
	 * @param m
	 *            a map containing the initial values of this object
	 * @throws NullPointerException
	 *             if the specified map is {@code null}
	 */
	public AbstractOpenServiceBrokerObject(Map<String, Object> m) {
		super(m);
	}

	/**
	 * Creates and adds a new object.
	 * 
	 * @param key
	 *            the key of the new object
	 * 
	 * @return the newly created object
	 */
	public JSONObject createObject(String key) {
		JSONObject result = new JSONObjectImpl();
		put(key, result);
		return result;
	}

	/**
	 * Creates and adds a new array.
	 * 
	 * @param <T>
	 *            the type of the array elements
	 * @param key
	 *            the key of the new array
	 * @param clazz
	 *            the class of the elements of the array
	 * 
	 * @return the newly created array
	 */
	public <T> JSONArray<T> createArray(String key, Class<T> clazz) {
		return createArray(key, clazz, null);
	}

	/**
	 * Creates, fills, and adds a new array.
	 * 
	 * @param <T>
	 *            the type of the array elements
	 * @param key
	 *            the key of the new array
	 * @param clazz
	 *            the class of the elements of the array
	 * @param values
	 *            initial values of the array
	 * 
	 * @return the newly created array
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
	 * 
	 * @param <T>
	 *            the type of the array elements
	 * @param key
	 *            the key of the new array
	 * @param clazz
	 *            the class of the elements of the array
	 * @param values
	 *            the values to add
	 * 
	 * @return the updated or created array
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
	 * 
	 * @param <T>
	 *            the type of the value
	 * @param key
	 *            the field key
	 * @param clazz
	 *            the expected class of the value
	 * 
	 * @return the value
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
	 * 
	 * @param <T>
	 *            the type of the array elements
	 * @param key
	 *            the field key
	 * @param clazz
	 *            the expected class of the elements in the array
	 * 
	 * @return the array
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
	 * 
	 * @param key
	 *            the field key
	 * 
	 * @return the value
	 */
	public String getString(String key) {
		return get(key, String.class);
	}

	/**
	 * Gets and casts a value of a field to a Long.
	 * 
	 * @param key
	 *            the field key
	 *
	 * @return the value
	 */
	public Long getNumber(String key) {
		return get(key, Long.class);
	}

	/**
	 * Gets and casts a value of a field to a Boolean.
	 * 
	 * @param key
	 *            the field key
	 * 
	 * @return the value
	 */
	public Boolean getBoolean(String key) {
		return get(key, Boolean.class);
	}

	/**
	 * Gets and casts a value of a field to a {@link JSONObject}.
	 * 
	 * @param key
	 *            the field key
	 * 
	 * @return the JSON object
	 */
	public JSONObject getJSONObject(String key) {
		return get(key, JSONObject.class);
	}

	/**
	 * Checks if a field value exists, is {@code null}, is empty string, or an empty
	 * list.
	 * 
	 * @param key
	 *            the field key
	 * 
	 * @return {@code true} if the field does not exist, its value is {@code null}
	 *         or an empty string or an empty list, {@code false} otherwise
	 */
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

	/**
	 * Checks if a field value is valid CLI name as defined in the OSBAPI
	 * specification.
	 * 
	 * @param key
	 *            the field key
	 * 
	 * @return {@code true} if the field exists, its value is string, and the string
	 *         is a valid CLI name, {@code false} otherwise
	 */
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

	/**
	 * Checks if a field value is valid ID as defined in the OSBAPI specification.
	 * 
	 * @param key
	 *            the field key
	 * 
	 * @return {@code true} if the field exists, its value is string, and the string
	 *         is a valid ID, {@code false} otherwise
	 */
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

	/**
	 * Walks through all fields in this object and validates their values.
	 * 
	 * @throws ValidationException
	 *             if a validation fails
	 */
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
	 * 
	 * @param in
	 *            the reader providing the JSON
	 * 
	 * @throws IOException
	 *             if reading or parsing fails
	 */
	public void load(Reader in) throws IOException {
		if (in == null) {
			throw new IllegalArgumentException("Reader must not be null!");
		}

		JSONParser parser = new JSONParser();
		try {
			parser.parse(in, this);
		} catch (JSONParseException e) {
			throw new IOException("Invalid JSON!", e);
		}
	}

	/**
	 * Loads a JSON into this object.
	 * 
	 * @param json
	 *            the JSON string
	 * 
	 * @throws IOException
	 *             if reading or parsing fails
	 */
	public void load(String json) throws IOException {
		if (json == null || json.isEmpty()) {
			throw new IllegalArgumentException("String must not be null or empty!");
		}

		JSONParser parser = new JSONParser();
		try {
			parser.parse(json, this);
		} catch (JSONParseException e) {
			throw new IOException("Invalid JSON!", e);
		}
	}
}
