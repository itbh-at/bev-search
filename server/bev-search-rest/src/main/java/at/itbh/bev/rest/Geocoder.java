package at.itbh.bev.rest;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import at.itbh.bev.api.BevFinder;
import at.itbh.bev.api.CommonBevFinder;
import at.itbh.bev.api.data.AustrianCommonQueryResult;
import at.itbh.bev.api.data.BevQueryResult;
import at.itbh.bev.api.data.SerializedGeocodingResult;
import at.itbh.bev.api.exceptions.InvalidApiUsageException;

@Path("/")
public class Geocoder {

	@Inject
	BevFinder finder;

	@Inject
	CommonBevFinder commonFinder;

	@GET
	@Path("/geocode")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SerializedGeocodingResult<BevQueryResult> geocode(
			@QueryParam("postalCode") @DefaultValue("") String postalCode,
			@QueryParam("place") @DefaultValue("") String place,
			@QueryParam("addressLine") @DefaultValue("") String addressLine,
			@QueryParam("houseId") @DefaultValue("") String houseId, @QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude, @QueryParam("radius") Float radius) {
		SerializedGeocodingResult<BevQueryResult> result = new SerializedGeocodingResult<BevQueryResult>(
				BevQueryResult.class);

		try {
			if (latitude != null && longitude != null) {
				if (radius != null) {
					result.setGeocodingResult(
							finder.geocode(postalCode, place, addressLine, houseId, latitude, longitude, radius));
				} else {
					result.setGeocodingResult(
							finder.geocode(postalCode, place, addressLine, houseId, latitude, longitude));
				}
			} else {
				result.setGeocodingResult(finder.geocode(postalCode, place, addressLine, houseId));
			}
		} 
		catch (InvalidApiUsageException e) {
			result.setException(e, true);
		}
		catch (Exception e) {
			result.setException(e, false);
		}
		
		return result;
	}

	@GET
	@Path("/geocode/{latitude}/{longitude}/{radius}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SerializedGeocodingResult<BevQueryResult> reverseGeocodeWithRadius(@PathParam("latitude") Double latitude,
			@PathParam("longitude") Double longitude, @PathParam("radius") Float radius) {
		SerializedGeocodingResult<BevQueryResult> result = new SerializedGeocodingResult<BevQueryResult>(
				BevQueryResult.class);
		try {
			result.setGeocodingResult(finder.reverseGeocode(latitude, longitude, radius));
		} 
		catch (InvalidApiUsageException e) {
			result.setException(e, true);
		}
		catch (Exception e) {
			result.setException(e, false);
		}
		return result;
	}

	/**
	 * Find addresses inside a radius of 0.5 km
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@GET
	@Path("/geocode/{latitude}/{longitude}/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SerializedGeocodingResult<BevQueryResult> reverseGeocode(@PathParam("latitude") Double latitude,
			@PathParam("longitude") Double longitude) {
		SerializedGeocodingResult<BevQueryResult> result = new SerializedGeocodingResult<BevQueryResult>(
				BevQueryResult.class);
		try {
			result.setGeocodingResult(finder.reverseGeocode(latitude, longitude, 0.5f));
		} catch (Exception e) {
			result.setException(e, false);
		}
		return result;
	}

	@GET
	@Path("/common/geocode")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SerializedGeocodingResult<AustrianCommonQueryResult> geocodeCommon(
			@QueryParam("postalCode") @DefaultValue("") String postalCode,
			@QueryParam("place") @DefaultValue("") String place,
			@QueryParam("addressLine") @DefaultValue("") String addressLine,
			@QueryParam("houseId") @DefaultValue("") String houseId, @QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude, @QueryParam("radius") Float radius) {
		SerializedGeocodingResult<AustrianCommonQueryResult> result = new SerializedGeocodingResult<AustrianCommonQueryResult>(
				AustrianCommonQueryResult.class);

		try {
			if (latitude != null && longitude != null) {
				if (radius != null) {
					result.setGeocodingResult(
							commonFinder.geocode(postalCode, place, addressLine, houseId, latitude, longitude, radius));
				} else {
					result.setGeocodingResult(
							commonFinder.geocode(postalCode, place, addressLine, houseId, latitude, longitude));
				}
			} else {
				result.setGeocodingResult(commonFinder.geocode(postalCode, place, addressLine, houseId));
			}
		} 
		catch (InvalidApiUsageException e) {
			result.setException(e, true);
		}
		catch (Exception e) {
			result.setException(e, false);
		}
		return result;
	}

	@GET
	@Path("/common/geocode/{latitude}/{longitude}/{radius}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SerializedGeocodingResult<AustrianCommonQueryResult> reverseGeocodeWithRadiusCommon(@PathParam("latitude") Double latitude,
			@PathParam("longitude") Double longitude, @PathParam("radius") Float radius) {
		SerializedGeocodingResult<AustrianCommonQueryResult> result = new SerializedGeocodingResult<AustrianCommonQueryResult>(
				AustrianCommonQueryResult.class);
		try {
			result.setGeocodingResult(commonFinder.reverseGeocode(latitude, longitude, radius));
		} catch (Exception e) {
			result.setException(e, false);
		}
		return result;
	}

	/**
	 * Find addresses inside a radius of 0.5 km
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@GET
	@Path("/common/geocode/{latitude}/{longitude}/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SerializedGeocodingResult<AustrianCommonQueryResult> reverseGeocodeCommon(@PathParam("latitude") Double latitude,
			@PathParam("longitude") Double longitude) {
		SerializedGeocodingResult<AustrianCommonQueryResult> result = new SerializedGeocodingResult<AustrianCommonQueryResult>(
				AustrianCommonQueryResult.class);
		try {
			result.setGeocodingResult(commonFinder.reverseGeocode(latitude, longitude, 0.5f));
		} catch (Exception e) {
			result.setException(e, false);
		}
		return result;
	}	
}
