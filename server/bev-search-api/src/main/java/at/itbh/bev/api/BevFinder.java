package at.itbh.bev.api;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import at.itbh.bev.api.data.BevAddress;
import at.itbh.bev.api.data.BevQueryResult;
import at.itbh.bev.api.data.GeocodingResult;
import at.itbh.bev.api.exceptions.InvalidApiUsageException;
import at.itbh.bev.api.exceptions.TooManyResultsException;

/**
 * The main interface for search the BEV data providing detailed address and
 * building information.
 */
@Local
public interface BevFinder {

	/**
	 * Find an address written similar to the provided one
	 * 
	 * @param postalCode
	 *            the 4 digit <code>PLZ</code> code. May be <code>null</code>.
	 * @param place
	 *            the place or municipality the address lies in. May be
	 *            <code>null</code>.
	 * @param addressLine
	 *            mostly the name of the street but also the name of the
	 *            building (<code>HOFNAME</code>) is valid input. May be
	 *            <code>null</code>.
	 * @param houseId
	 *            commonly referred to a house number but also house number and
	 *            staircase written as <code>house number/staircase</code> is
	 *            valid as well as building identifiers like
	 *            <code>Obj. 274</code> are valid input. May be
	 *            <code>null</code>.
	 * @return
	 * @throws InvalidApiUsageException 
	 */
	@PermitAll
	public GeocodingResult<BevQueryResult> geocode(String postalCode, String place, String addressLine, String houseId) throws InvalidApiUsageException;

	/**
	 * Search for a similar address 1km around the search center defined by
	 * <code>latitude</code> and <code>longitude</code>-
	 * @throws InvalidApiUsageException 
	 * 
	 * @see BevFinder#geocode(String, String, String, String)
	 */
	public GeocodingResult<BevQueryResult> geocode(String postalCode, String place, String addressLine, String houseId,
			Double latitude, Double longitude) throws InvalidApiUsageException;
	
	/**
	 * Search for a similar address <code>radius</code> km around the search center defined by
	 * <code>latitude</code> and <code>longitude</code>-
	 * @throws InvalidApiUsageException 
	 * 
	 * @see BevFinder#geocode(String, String, String, String)
	 */
	public GeocodingResult<BevQueryResult> geocode(String postalCode, String place, String addressLine, String houseId,
			Double latitude, Double longitude, Float radius) throws InvalidApiUsageException;


	/**
	 * Find addresses near to the provided GPS coordinates (EPSG:4326)
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 *            The radius in km to search around the given coordinates for
	 *            addresses in meters. It defaults to 500m.
	 * @return a list of addresses inside the given radius sorted by their
	 *         distance to the provided coordinates in ascending order.
	 */
	@PermitAll
	public GeocodingResult<BevQueryResult> reverseGeocode(@NotNull Double latitude, @NotNull Double longitude, Float radius) throws InvalidApiUsageException;

	/**
	 * Lookup addresses by the BEV id <code>ADRCD</code>
	 * 
	 * @param adrcd
	 *            the unique id of <code>ADRESSE</code>
	 * @return a list of <code>ADRESSE</code> and
	 *         <code>GEBAEUDE<code> combinations matching this <code>ADRCD</code>
	 *         . It no data has been found the empty list is returned.
	 */
	@PermitAll
	public List<BevAddress> findAddressByADRCD(@NotNull String adrcd);

	/**
	 * Lookup a unique address in the BEV data
	 * 
	 * @param adrcd
	 *            The unique identifier of a BEV <code>ADRESSE</code>. It must
	 *            no be <code>null</code>.
	 * @param subcd
	 *            In combination with <code>ADRCD</code> the unique identifier
	 *            of a <code>GEBAEUDE</code>. It may be <code>null</code.>
	 * @return an address or <code>null</code> if no address has been found
	 */
	@PermitAll
	public BevAddress findAddress(@NotNull String adrcd, String subcd) throws TooManyResultsException;

	/**
	 * Lookup a unique address in the BEV data
	 * 
	 * <p>
	 * The id used to retrieve an address is <i>not</i> an identifier provided
	 * by BEV. It is a combination of the unique identifiers of the tables
	 * <code>ADRESSE</code> and <code>GEBAEUDE</code>. The id has the form
	 * <code>ADRCD-SUBCD</code> where <code>SUBCD</code> can be
	 * <code>null</code> if no <code>GEBAEUDE</code> is associated with this
	 * <code>ADRESSE</code>.
	 * </p>
	 * 
	 * @param id
	 *            The id computed for each <code>ADRESSE</code> combined with
	 *            <code>GEBAEUDE</code> at import time.
	 * @return an address or <code>null</code> if no address has been found
	 */
	@PermitAll
	public BevAddress findAddress(@NotNull String id);
}
