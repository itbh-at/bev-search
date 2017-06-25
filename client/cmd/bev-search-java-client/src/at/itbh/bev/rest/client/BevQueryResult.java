package at.itbh.bev.rest.client;

import java.util.Map;

public class BevQueryResult {

	private String postalCode;
	private String place;
	private String street;
	private String houseNumber;
	private Object houseNumberAddition;
	private String buildingId;
	private String addressName;
	private String buildingName;
	private String municipality;
	private String longitude;
	private String latitude;
	private String id;
	private String adrcd;
	private String subcd;
	private Object skz;
	private String okz;
	private String gkz;
	private double score;
	private String distance;
	private boolean warning = false;
	private boolean foundMatch = false;
	
	/**
	 * Original input data to be available in a later processing phase
	 */
	private Map<String, String> inputData;

	public Map<String, String> getInputData() {
		return inputData;
	}

	public void setInputData(Map<String, String> inputData) {
		this.inputData = inputData;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPlace() {
		return place;
	}

	public String getStreet() {
		return street;
	}

	public Integer getHouseNumber() {
		try {
			return Integer.parseInt(houseNumber);
		} catch (Exception e) {
			return null;
		}
	}

	public Object getHouseNumberAddition() {
		return houseNumberAddition;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public String getAddressName() {
		return addressName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public String getMunicipality() {
		return municipality;
	}

	public Double getLongitude() {
		try {
			return Double.parseDouble(longitude);
		}
		catch (Exception e) {
			return null;
		}
	}

	public Double getLatitude() {
		try {
			return Double.parseDouble(latitude);
		}
		catch (Exception e) {
			return null;
		}
	}

	public String getId() {
		return id;
	}

	public String getAdrcd() {
		return adrcd;
	}

	public String getSubcd() {
		return subcd;
	}

	public Object getSkz() {
		return skz;
	}

	public String getOkz() {
		return okz;
	}

	public String getGkz() {
		return gkz;
	}

	public double getScore() {
		return score;
	}

	public String getDistance() {
		return distance;
	}
	
	public boolean getWarning() {
		return warning;
	}
	
	public boolean getFoundMatch() {
		return foundMatch;
	}

	void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	void setPlace(String place) {
		this.place = place;
	}

	void setStreet(String street) {
		this.street = street;
	}

	void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	void setHouseNumberAddition(String houseNumberAddition) {
		this.houseNumberAddition = houseNumberAddition;
	}

	void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	void setId(String id) {
		this.id = id;
	}

	void setAdrcd(String adrcd) {
		this.adrcd = adrcd;
	}

	void setSubcd(String subcd) {
		this.subcd = subcd;
	}

	void setSkz(String skz) {
		this.skz = skz;
	}

	void setOkz(String okz) {
		this.okz = okz;
	}

	void setGkz(String gkz) {
		this.gkz = gkz;
	}

	void setScore(double score) {
		this.score = score;
	}

	void setDistance(String distance) {
		this.distance = distance;
	}

	void setWarning(boolean warning) {
		this.warning = warning;
	}
	
	void setFoundMatch(boolean foundMatch) {
		this.foundMatch = foundMatch;
	}

	@Override
	public String toString() {
		return "BevQueryResult [postalCode=" + postalCode + ", place=" + place + ", street=" + street + ", houseNumber="
				+ houseNumber + ", houseNumberAddition=" + houseNumberAddition + ", buildingId=" + buildingId
				+ ", addressName=" + addressName + ", buildingName=" + buildingName + ", municipality=" + municipality
				+ ", longitude=" + longitude + ", latitude=" + latitude + ", id=" + id + ", adrcd=" + adrcd + ", subcd="
				+ subcd + ", skz=" + skz + ", okz=" + okz + ", gkz=" + gkz + ", score=" + score + ", distance="
				+ distance + ", warning=" + warning + ", foundMatch=" + foundMatch + ", inputData=" + inputData + "]";
	}

}
