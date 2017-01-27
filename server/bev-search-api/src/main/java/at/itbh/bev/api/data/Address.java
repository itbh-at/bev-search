package at.itbh.bev.api.data;

public interface Address {

	/**
	 * @return the unique identifier of this entity
	 */
	public String getId();

	/**
	 * @return the BEV identifier of the address
	 */
	public String getAdrcd();

	/**
	 * @return in combination with {@link #getAdrcd()} the unique identifier of
	 *         a building belonging to an address. It may be <code>null</code>
	 *         if no building belongs to the address.
	 */
	public String getSubcd();
	
	public String getOkz();
	
	public String getSkz();
	
	public String getGkz();

	public Double getLatitude();
	
	public Double getLongitude();
}