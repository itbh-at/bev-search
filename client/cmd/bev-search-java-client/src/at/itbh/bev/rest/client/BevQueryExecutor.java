package at.itbh.bev.rest.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Schedule queries to the ReST endpoint
 */
public class BevQueryExecutor {

	private final ExecutorService fixedThreadPool;
	private WebTarget restEndpoint;

	public BevQueryExecutor(WebTarget restEndpoint, int numThreads) {
		this.restEndpoint = restEndpoint;
		this.fixedThreadPool = Executors.newFixedThreadPool(numThreads);
	}

	/**
	 * Free resources
	 */
	public void dispose() {
		fixedThreadPool.shutdown();
	}

	/**
	 * Schedule a query to the ReSt endpoint
	 * 
	 * @param restEndpoint
	 *            URL of the ReST endpoint
	 * @param postalCode
	 * @param place
	 * @param addressLine
	 * @param houseId
	 * @param radius
	 * @param longitude
	 * @param latitude
	 * @param enforceUnique
	 *            If <code>true</code> the result object is added only to the
	 *            result list if it is unique. Otherwise the empty list is
	 *            returned.
	 */
	public Future<List<BevQueryResult>> query(final String postalCode, final String place, final String addressLine, final String houseId,
			final String longitude, final String latitude, final String radius, final boolean enforceUnique) {
		// Create asynchronous request
		Callable<List<BevQueryResult>> request = new Callable<List<BevQueryResult>>() {
			@Override
			public List<BevQueryResult> call() throws Exception {
				if (postalCode != null) {
					restEndpoint = restEndpoint.queryParam("postalCode", postalCode);
				}
				if (place != null) {
					restEndpoint = restEndpoint.queryParam("place", place);
				}
				if (addressLine != null) {
					restEndpoint = restEndpoint.queryParam("addressLine", addressLine);
				}
				if (houseId != null) {
					restEndpoint = restEndpoint.queryParam("houseId", houseId);
				}
				if (radius != null) {
					restEndpoint = restEndpoint.queryParam("radius", radius);
				}
				if (longitude != null) {
					restEndpoint = restEndpoint.queryParam("longitude", longitude);
				}
				if (latitude != null) {
					restEndpoint = restEndpoint.queryParam("latitude", latitude);
				}

				// Build a HTTP GET request
				Invocation invocation = restEndpoint.request(MediaType.APPLICATION_JSON).buildGet();
				
				// Invoke the request using the generic interface
				String response = invocation.invoke(String.class);

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode root = objectMapper.readTree(response);

				String type = root.at("/status/responseType").asText();

				if (!type.equals("at.itbh.bev.api.data.AustrianCommonQueryResult")) {
					throw new Exception(
							"Only response type 'at.itbh.bev.api.data.AustrianCommonQueryResult' is supported.");
				}

				Integer code = root.at("/status/code").asInt();
				if (code > 0) {
					String message = root.at("/status/message").asText();
					throw new BevRestException(code, message);
				}

				Iterator<JsonNode> iter = root.at("/response").elements();
				List<BevQueryResult> results = new ArrayList<BevQueryResult>();
				int i = 0;
				while (iter.hasNext()) {
					// Stop retrieving data if unique result is enforced and
					// there is already one result. In this case the result is
					// a list with a single and empty result which is set to no
					// match found.
					if (enforceUnique && i > 0) {
						results.clear();
						BevQueryResult tempResult = new BevQueryResult();
						tempResult.setFoundMatch(false);
						results.add(tempResult);
						return results;
					}

					JsonNode node = iter.next();
					BevQueryResult bqr = new BevQueryResult();

					bqr.setPostalCode(node.at("/address/postalCode").asText(""));
					bqr.setPlace(node.at("/address/place").asText(""));
					bqr.setStreet(node.at("/address/street").asText(""));
					bqr.setHouseNumber(node.at("/address/houseNumber").asText(""));
					bqr.setHouseNumberAddition(node.at("/address/houseNumberAddition").asText(""));
					bqr.setBuildingId(node.at("/address/buildingId").asText(""));
					bqr.setAddressName(node.at("/address/addressName").asText(""));
					bqr.setBuildingName(node.at("/address/buildingName").asText(""));
					bqr.setMunicipality(node.at("/address/municipality").asText(""));
					bqr.setLongitude(node.at("/address/longitude").asText(""));
					bqr.setLatitude(node.at("/address/latitude").asText(""));
					bqr.setId(node.at("/address/id").asText(""));
					bqr.setAdrcd(node.at("/address/adrcd").asText(""));
					bqr.setSubcd(node.at("/address/subcd").asText(""));
					bqr.setSkz(node.at("/address/skz").asText(""));
					bqr.setOkz(node.at("/address/okz").asText(""));
					bqr.setGkz(node.at("/address/gkz").asText(""));
					bqr.setScore(node.at("/score").asDouble());
					bqr.setDistance(node.at("/distance").asText(""));
					bqr.setWarning(node.at("/warning").asBoolean(false));

					results.add(bqr);
					i++;
				}
				return results;
			}
		};
		return fixedThreadPool.submit(request);
	}

}
