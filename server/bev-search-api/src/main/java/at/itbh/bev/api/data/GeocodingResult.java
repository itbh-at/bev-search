/* 
 * BevGeocodingResult.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The result object for geocoding requests
 */
public class GeocodingResult<T extends QueryResult> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static class Request implements Serializable {

		private static final long serialVersionUID = 1L;

		private Double latitude;
		private Double longitude;
		private String postalCode;
		private String place;
		private String addressLine;
		private String houseId;

		private Float radius;

		public Double getLatitude() {
			return latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public String getPlace() {
			return place;
		}

		public String getAddressLine() {
			return addressLine;
		}

		public String getHouseId() {
			return houseId;
		}

		public Float getRadius() {
			return radius;
		}

		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public void setAddressLine(String addressLine) {
			this.addressLine = addressLine;
		}

		public void setHouseId(String houseId) {
			this.houseId = houseId;
		}

		public void setRadius(Float radius) {
			this.radius = radius;
		}

	}

	private Request request = new Request();

	private List<T> response = new ArrayList<T>();

	public Request getRequest() {
		return request;
	}

	/**
	 * All query results
	 *
	 * @return an unmodifiable list containing all query results
	 */
	public List<T> getResponse() {
		return Collections.unmodifiableList(response);
	}

	/**
	 * Add the result if <code>result</code> is not <code>null</code>
	 * @param result
	 */
	public void addResult(T result) {
		if (result != null)
			this.response.add(result);
	}

	protected void setResponse(List<T> response) {
		this.response = response;
	}

	protected void setRequest(Request request) {
		this.request = request;
	}

}
