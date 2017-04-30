/* 
 * SerializedGeocodingResult.java
 *  
 * Copyright (C) 2017 Christoph D. Hermann <christoph.hermann@itbh.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.itbh.bev.api.data;

import java.io.Serializable;

/**
 * A geocoding result object for remote clients having to serialize the result
 * like in ReST oder SOAP request. It is basically the same as
 * {@link GeocodingResult} but features a status object.
 * 
 * <p>
 * The {@link Status} object contains a status code {@link Status#getCode()}
 * featuring the following values:
 * <ul>
 * <li><code>0</code>: no error, everything worked fine</li>
 * <li><code>1</code>: some error occurred in the business logic</li>
 * <li><code>2</code>: something was wrong in the request</li>
 * </ul>
 * Additionally, {@link Status#getMessage()} should contain a description of the
 * error if {@link Status#getCode()} is not equal 0.
 * </p>
 * 
 * @param <T>
 *            Type of the returned {@link QueryResult}
 */
public class SerializedGeocodingResult<T extends QueryResult> extends GeocodingResult<T> {

	private static final long serialVersionUID = 1L;

	public static class Status implements Serializable {

		private static final long serialVersionUID = 1L;

		public final static Integer SUCCESS = 0;
		public final static Integer ERROR = 1;
		public final static Integer ERROR_REQUEST = 2;

		private Integer code = SUCCESS;
		private String message;
		private String responseType;

		public Integer getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public String getResponseType() {
			return responseType;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		void setResponseType(String responseType) {
			this.responseType = responseType;
		}

	}

	private Status status = new Status();

	public SerializedGeocodingResult(Class<T> type) {
		this.getStatus().setResponseType(type.getName());
	}

	public Status getStatus() {
		return status;
	}

	/**
	 * Represent the exception as a status code and error message inside
	 * {@link #getStatus()}
	 * 
	 * @param e
	 *            the exception to be wrapped
	 * @param isRequestError
	 *            <code>true</code> if the reason for the error is wrong
	 *            interface API usage, otherwise <code>false</code>. This
	 *            results in a different status code.
	 */
	public void setException(Exception e, boolean isRequestError) {
		if (isRequestError) {
			getStatus().setCode(Status.ERROR_REQUEST);
		} else {
			getStatus().setCode(Status.ERROR);
		}
		getStatus().setMessage(e.getMessage());
	}

	@Override
	public void addResult(T result) {
		super.addResult(result);
	}

	/**
	 * Link to the objects in {@link GeocodingResult#getResponse()} and {@link GeocodingResult#getRequest()} of the parameter <code>result</code>
	 * 
	 * @param result the {@link GeocodingResult} to be wrapped
	 */
	public void setGeocodingResult(GeocodingResult<T> result) {
		setResponse(result.getResponse());
		setRequest(result.getRequest());
	}

}
