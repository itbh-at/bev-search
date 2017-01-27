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
