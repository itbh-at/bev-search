package at.itbh.bev.rest.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
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
	private String restEndpoint;
	private Client client;

	public BevQueryExecutor(Client client, String restEndpoint, int numThreads) {
		this.client = client;
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
	 * @param inputData
	 *            must not be <code>null<code>. All query parameters and
	 *            additional input fields to be passed through to the
	 *            {@link BevQueryResult} in
	 *            {@link BevQueryResult#getInputData()} as key-value pairs.
	 */
	public Future<List<BevQueryResult>> query(final String postalCode, final String place, final String addressLine,
			final String houseId, final String longitude, final String latitude, final String radius,
			final boolean enforceUnique, Map<String, String> inputData) {

		inputData.put(BevRestClient.INPUT_POSTAL_CODE, postalCode);
		inputData.put(BevRestClient.INPUT_PLACE, place);
		inputData.put(BevRestClient.INPUT_ADDRESS_LINE, addressLine);
		inputData.put(BevRestClient.INPUT_HOUSE_ID, houseId);
		inputData.put(BevRestClient.INPUT_LONGITUDE, longitude);
		inputData.put(BevRestClient.INPUT_LATITUDE, latitude);
		inputData.put(BevRestClient.INPUT_RADIUS, radius);
		inputData.put(BevRestClient.INPUT_ENFORCE_UNIQUE, Objects.toString(enforceUnique));

		// Create asynchronous request
		Callable<List<BevQueryResult>> request = new Callable<List<BevQueryResult>>() {
			@Override
			public List<BevQueryResult> call() throws Exception {
				WebTarget target = client.target(restEndpoint);

				if (postalCode != null) {
					target = target.queryParam("postalCode", postalCode);
				}
				if (place != null) {
					target = target.queryParam("place", place);
				}
				if (addressLine != null) {
					target = target.queryParam("addressLine", addressLine);
				}
				if (houseId != null) {
					target = target.queryParam("houseId", houseId);
				}
				if (radius != null) {
					target = target.queryParam("radius", radius);
				}
				if (longitude != null) {
					target = target.queryParam("longitude", longitude);
				}
				if (latitude != null) {
					target = target.queryParam("latitude", latitude);
				}

				// Build a HTTP GET request
				Invocation invocation = target.request(MediaType.APPLICATION_JSON).buildGet();

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
					// don't throw an exception if the query is considered being
					// empty
					if (!message.contains("org.hibernate.search.exception.EmptyQueryException")) {
						throw new BevRestException(code, message);
					}
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
						tempResult.setInputData(inputData);
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
					bqr.setFoundMatch(true);

					bqr.setInputData(inputData);

					results.add(bqr);
					i++;
				}
				return results;
			}
		};
		return fixedThreadPool.submit(request);
	}

}
