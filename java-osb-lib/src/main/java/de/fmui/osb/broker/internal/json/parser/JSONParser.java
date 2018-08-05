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
package de.fmui.osb.broker.internal.json.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import de.fmui.osb.broker.internal.Constraints;
import de.fmui.osb.broker.internal.json.JSONArrayImpl;
import de.fmui.osb.broker.internal.json.JSONObjectImpl;
import de.fmui.osb.broker.json.JSONArray;
import de.fmui.osb.broker.json.JSONObject;
import de.fmui.osb.broker.objects.KeyMapping;
import de.fmui.osb.broker.objects.KeyMappings;

/**
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe.
 */
public class JSONParser {
	public static final int S_INIT = 0;
	public static final int S_IN_FINISHED_VALUE = 1; // string, number, boolean,
														// null, object, array
	public static final int S_IN_OBJECT = 2;
	public static final int S_IN_ARRAY = 3;
	public static final int S_PASSED_PAIR_KEY = 4;
	public static final int S_IN_PAIR_VALUE = 5;
	public static final int S_END = 6;
	public static final int S_IN_ERROR = -1;

	private Yylex lexer = new Yylex((Reader) null);
	private Yytoken token = null;
	private int status = S_INIT;

	private int peekStatus(Deque<Integer> statusStack) {
		if (statusStack.isEmpty()) {
			return -1;
		}

		return statusStack.getFirst();
	}

	/**
	 * Reset the parser to the initial state without resetting the underlying
	 * reader.
	 * 
	 */
	public void reset() {
		token = null;
		status = S_INIT;
	}

	/**
	 * Reset the parser to the initial state with a new character reader.
	 * 
	 * @param in
	 *            the new character reader
	 */
	public void reset(Reader in) {
		lexer.yyreset(in);
		reset();
	}

	/**
	 * @return The position of the beginning of the current token.
	 */
	public int getPosition() {
		return lexer.getPosition();
	}

	public <T> T parse(String s, T root) throws JSONParseException {
		StringReader in = new StringReader(s);
		try {
			return parse(in, root);
		} catch (IOException ie) {
			/*
			 * Actually it will never happen.
			 */
			throw new JSONParseException(-1, JSONParseException.ErrorType.ERROR_UNEXPECTED_EXCEPTION, ie);
		}
	}

	/**
	 * Parse JSON text into java object from the input source.
	 * 
	 * @param in
	 * @return Instance of the following: JSONObject, JSONArray, java.lang.String,
	 *         java.lang.Number, java.lang.Boolean, null
	 */
	@SuppressWarnings("unchecked")
	public <T> T parse(Reader in, T root) throws IOException, JSONParseException {
		reset(in);
		Deque<Integer> statusStack = new ArrayDeque<Integer>();
		Deque<Object> valueStack = new ArrayDeque<Object>();

		try {
			do {
				nextToken();
				switch (status) {
				case S_INIT:
					switch (token.type) {
					case Yytoken.TYPE_VALUE:
						status = S_IN_FINISHED_VALUE;
						// statusStack.addFirst(Integer.valueOf(status));
						// valueStack.addFirst(token.value);
						// break;
						throw new JSONParseException(getPosition(), JSONParseException.ErrorType.ERROR_UNEXPECTED_ROOT,
								"Root object must not be a value!");
					case Yytoken.TYPE_LEFT_BRACE:
						status = S_IN_OBJECT;
						statusStack.addFirst(Integer.valueOf(status));
						valueStack.addFirst(root);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status = S_IN_ARRAY;
						// statusStack.addFirst(Integer.valueOf(status));
						// valueStack.addFirst(createArrayContainer());
						// break;
						throw new JSONParseException(getPosition(), JSONParseException.ErrorType.ERROR_UNEXPECTED_ROOT,
								"Root object must not be an array!");
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;

				case S_IN_FINISHED_VALUE:
					if (token.type == Yytoken.TYPE_EOF) {
						return (T) valueStack.removeFirst();
					} else {
						throw new JSONParseException(getPosition(), JSONParseException.ErrorType.ERROR_UNEXPECTED_TOKEN,
								token);
					}

				case S_IN_OBJECT:
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if (token.value instanceof String) {
							String key = (String) token.value;
							valueStack.addFirst(key);
							status = S_PASSED_PAIR_KEY;
							statusStack.addFirst(Integer.valueOf(status));
						} else {
							status = S_IN_ERROR;
						}
						break;
					case Yytoken.TYPE_RIGHT_BRACE:
						if (valueStack.size() > 1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						break;
					default:
						status = S_IN_ERROR;
						break;
					}// inner switch
					break;

				case S_PASSED_PAIR_KEY:
					switch (token.type) {
					case Yytoken.TYPE_COLON:
						break;
					case Yytoken.TYPE_VALUE:
						statusStack.removeFirst();
						String key = (String) valueStack.removeFirst();
						Map<String, Object> parent = (Map<String, Object>) valueStack.getFirst();
						if (parent.size() + 1 > Constraints.MAX_OBJECT_SIZE) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						parent.put(key, token.value);
						status = peekStatus(statusStack);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						statusStack.removeFirst();
						key = (String) valueStack.removeFirst();
						parent = (Map<String, Object>) valueStack.getFirst();
						if (parent.size() + 1 > Constraints.MAX_OBJECT_SIZE) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						if (valueStack.size() + 1 > Constraints.MAX_DEPTH) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						JSONArray<?> newArray = createArrayContainer(parent.getClass(), key);
						parent.put(key, newArray);
						status = S_IN_ARRAY;
						statusStack.addFirst(Integer.valueOf(status));
						valueStack.addFirst(newArray);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						statusStack.removeFirst();
						key = (String) valueStack.removeFirst();
						parent = (Map<String, Object>) valueStack.getFirst();
						if (parent.size() + 1 > Constraints.MAX_OBJECT_SIZE) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						if (valueStack.size() + 1 > Constraints.MAX_DEPTH) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						Map<String, Object> newObject = createObjectContainer(parent.getClass(), key);
						parent.put(key, newObject);
						status = S_IN_OBJECT;
						statusStack.addFirst(Integer.valueOf(status));
						valueStack.addFirst(newObject);
						break;
					default:
						status = S_IN_ERROR;
					}
					break;

				case S_IN_ARRAY:
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						JSONArrayImpl<Object> val = (JSONArrayImpl<Object>) valueStack.getFirst();
						if (val.size() + 1 > Constraints.MAX_ARRAY_SIZE) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						val.add(token.value);
						break;
					case Yytoken.TYPE_RIGHT_SQUARE:
						if (valueStack.size() > 1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						val = (JSONArrayImpl<Object>) valueStack.getFirst();
						if (val.size() + 1 > Constraints.MAX_ARRAY_SIZE) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						if (valueStack.size() + 1 > Constraints.MAX_DEPTH) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						Map<String, Object> newObject = createObjectContainer(val);
						val.add(newObject);
						status = S_IN_OBJECT;
						statusStack.addFirst(Integer.valueOf(status));
						valueStack.addFirst(newObject);
						break;

					case Yytoken.TYPE_LEFT_SQUARE:
						val = (JSONArrayImpl<Object>) valueStack.getFirst();
						if (val.size() + 1 > Constraints.MAX_ARRAY_SIZE) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						if (valueStack.size() + 1 > Constraints.MAX_DEPTH) {
							throw new JSONParseException(JSONParseException.ErrorType.ERROR_JSON_TOO_BIG);
						}
						JSONArrayImpl<?> newArray = val.createSubArray();
						val.add(newArray);
						status = S_IN_ARRAY;
						statusStack.addFirst(Integer.valueOf(status));
						valueStack.addFirst(newArray);
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;
				case S_IN_ERROR:
					throw new JSONParseException(getPosition(), JSONParseException.ErrorType.ERROR_UNEXPECTED_TOKEN,
							token);
				}// switch
				if (status == S_IN_ERROR) {
					throw new JSONParseException(getPosition(), JSONParseException.ErrorType.ERROR_UNEXPECTED_TOKEN,
							token);
				}
			} while (token.type != Yytoken.TYPE_EOF);
		} catch (IOException ie) {
			throw ie;
		}

		throw new JSONParseException(getPosition(), JSONParseException.ErrorType.ERROR_UNEXPECTED_TOKEN, token);
	}

	private void nextToken() throws JSONParseException, IOException {
		token = lexer.yylex();
		if (token == null) {
			token = new Yytoken(Yytoken.TYPE_EOF, null);
		}
	}

	private JSONObject createObjectContainer(Class<?> parent, String key) {
		Class<?> clazz = getMappingClass(parent, key);
		if (clazz == null) {
			return new JSONObjectImpl();
		} else {
			try {
				return clazz.asSubclass(JSONObject.class).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Cannot create object '" + clazz.getSimpleName() + "': " + e.toString(), e);
			}
		}
	}

	private JSONObject createObjectContainer(JSONArrayImpl<?> array) {
		if (!JSONObject.class.isAssignableFrom(array.getArrayClass())) {
			return new JSONObjectImpl();
		} else {
			try {
				return array.getArrayClass().asSubclass(JSONObject.class).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot create object '" + array.getArrayClass().getSimpleName() + "': " + e.toString(), e);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONArray<?> createArrayContainer(Class<?> parent, String key) {
		Class<?> clazz = getMappingClass(parent, key);
		if (clazz == null) {
			clazz = Object.class;
		}

		return new JSONArrayImpl(clazz);
	}

	private Class<?> getMappingClass(Class<?> parent, String key) {
		if (parent != null) {
			// multiple annotations
			KeyMappings keyMappings = parent.getAnnotation(KeyMappings.class);
			if (keyMappings != null) {
				for (KeyMapping km : keyMappings.value()) {
					if (km.jsonKey().equals(key)) {
						try {
							return km.osbClass();
						} catch (Exception e) {
							throw new RuntimeException(
									"Cannot create object '" + km.osbClass().getSimpleName() + "': " + e.toString(), e);
						}
					}
				}
			}

			// single annotation
			KeyMapping keyMapping = parent.getAnnotation(KeyMapping.class);
			if (keyMapping != null) {
				if (keyMapping.jsonKey().equals(key)) {
					try {
						return keyMapping.osbClass();
					} catch (Exception e) {
						throw new RuntimeException(
								"Cannot create object '" + keyMapping.osbClass().getSimpleName() + "': " + e.toString(),
								e);
					}
				}
			}
		}

		return null;
	}
}
