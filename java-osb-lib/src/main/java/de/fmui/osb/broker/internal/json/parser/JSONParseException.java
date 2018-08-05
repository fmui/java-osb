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

/**
 * ParseException explains why and where the error occurs in source JSON text.
 */
public class JSONParseException extends Exception {
	private static final long serialVersionUID = 1;

	public enum ErrorType {
		ERROR_UNEXPECTED_CHAR, ERROR_UNEXPECTED_TOKEN, ERROR_UNEXPECTED_ROOT, ERROR_UNEXPECTED_EXCEPTION, ERROR_STRING_TOO_LONG, ERROR_JSON_TOO_BIG, ERROR_NUMBER_TOO_BIG
	}

	private ErrorType errorType;
	private Object unexpectedObject;
	private int position;

	public JSONParseException(ErrorType errorType) {
		this(-1, errorType, null);
	}

	public JSONParseException(ErrorType errorType, Object unexpectedObject) {
		this(-1, errorType, unexpectedObject);
	}

	public JSONParseException(int position, ErrorType errorType, Object unexpectedObject) {
		super();

		this.position = position;
		this.errorType = errorType;
		this.unexpectedObject = unexpectedObject;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder(128);

		switch (errorType) {
		case ERROR_UNEXPECTED_CHAR:
			sb.append("Unexpected character (").append(unexpectedObject).append(") at position ").append(position)
					.append('.');
			break;
		case ERROR_UNEXPECTED_TOKEN:
			sb.append("Unexpected token ").append(unexpectedObject).append(" at position ").append(position)
					.append('.');
			break;
		case ERROR_UNEXPECTED_ROOT:
			sb.append("Unexpected root object.");
			break;
		case ERROR_UNEXPECTED_EXCEPTION:
			sb.append("Unexpected exception at position ").append(position).append(": ").append(unexpectedObject);
			break;
		case ERROR_STRING_TOO_LONG:
			sb.append("String too long");
			break;
		case ERROR_JSON_TOO_BIG:
			sb.append("JSON too big");
			break;
		default:
			sb.append("Unkown error at position ").append(position).append('.');
			break;
		}

		return sb.toString();
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	/**
	 * @see JSONParser#getPosition()
	 * 
	 * @return The character position (starting with 0) of the input where the error
	 *         occurs.
	 */
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @see Yytoken
	 * 
	 * @return One of the following base on the value of errorType:
	 *         ERROR_UNEXPECTED_CHAR java.lang.Character ERROR_UNEXPECTED_TOKEN
	 *         Yytoken ERROR_UNEXPECTED_EXCEPTION java.lang.Exception
	 */
	public Object getUnexpectedObject() {
		return unexpectedObject;
	}

	public void setUnexpectedObject(Object unexpectedObject) {
		this.unexpectedObject = unexpectedObject;
	}

	@Override
	public String toString() {
		return getMessage();
	}
}
