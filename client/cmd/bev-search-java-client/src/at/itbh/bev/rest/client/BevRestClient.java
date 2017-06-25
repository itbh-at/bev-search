/* 
 * BevRestClient.java
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

package at.itbh.bev.rest.client;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class BevRestClient {

	/**
	 * Input field name
	 */
	public static final String INPUT_POSTAL_CODE = "postalCode";

	/**
	 * Input field name
	 */
	public static final String INPUT_PLACE = "place";

	/**
	 * Input field name
	 */
	public static final String INPUT_ADDRESS_LINE = "addressLine";

	/**
	 * Input field name
	 */
	public static final String INPUT_HOUSE_ID = "houseId";

	/**
	 * Input field name
	 */
	public static final String INPUT_LONGITUDE = "longitude";

	/**
	 * Input field name
	 */
	public static final String INPUT_LATITUDE = "latitude";

	/**
	 * Input field name
	 */
	public static final String INPUT_RADIUS = "radius";

	/**
	 * Input field name
	 */
	public static final String INPUT_ENFORCE_UNIQUE = "enforceUnique";

	/**
	 * Query result field name
	 */
	private static final String _FOUND_MATCH = "_foundMatch";

	/**
	 * Query result field name
	 */
	private static final String _DISTANCE = "_distance";
	/**
	 * Query result field name
	 */
	private static final String _SCORE = "_score";
	/**
	 * Query result field name
	 */
	private static final String _WARNING = "_warning";
	/**
	 * Query result field name
	 */
	private static final String _GKZ = "_gkz";
	/**
	 * Query result field name
	 */
	private static final String _OKZ = "_okz";
	/**
	 * Query result field name
	 */
	private static final String _SKZ = "_skz";
	/**
	 * Query result field name
	 */
	private static final String _SUBCD = "_subcd";
	/**
	 * Query result field name
	 */
	private static final String _ADRCD = "_adrcd";
	/**
	 * Query result field name
	 */
	private static final String _ID = "_id";
	/**
	 * Query result field name
	 */
	private static final String _LATITUDE = "_latitude";
	/**
	 * Query result field name
	 */
	private static final String _LONGITUDE = "_longitude";
	/**
	 * Query result field name
	 */
	private static final String _MUNICIPALITY = "_municipality";
	/**
	 * Query result field name
	 */
	private static final String _BUILDING_NAME = "_buildingName";
	/**
	 * Query result field name
	 */
	private static final String _ADDRESS_NAME = "_addressName";
	/**
	 * Query result field name
	 */
	private static final String _BUILDING_ID = "_buildingId";
	/**
	 * Query result field name
	 */
	private static final String _HOUSE_NUMBER_ADDITION = "_houseNumberAddition";
	/**
	 * Query result field name
	 */
	private static final String _HOUSE_NUMBER = "_houseNumber";
	/**
	 * Query result field name
	 */
	private static final String _STREET = "_street";
	/**
	 * Query result field name
	 */
	private static final String _PLACE = "_place";
	/**
	 * Query result field name
	 */
	protected static final String _POSTAL_CODE = "_postalCode";

	private Options options;
	private CommandLineParser parser = new DefaultParser();
	private ICsvMapWriter csvWriter = null;

	/**
	 * Fields contained in every output
	 */
	private final String[] defaultFieldNames = new String[] { INPUT_POSTAL_CODE, INPUT_PLACE, INPUT_ADDRESS_LINE,
			INPUT_HOUSE_ID, INPUT_LONGITUDE, INPUT_LATITUDE, INPUT_RADIUS, INPUT_ENFORCE_UNIQUE, _POSTAL_CODE, _PLACE,
			_STREET, _HOUSE_NUMBER, _HOUSE_NUMBER_ADDITION, _BUILDING_ID, _ADDRESS_NAME, _BUILDING_NAME, _MUNICIPALITY,
			_LONGITUDE, _LATITUDE, _ID, _ADRCD, _SUBCD, _SKZ, _OKZ, _GKZ, _WARNING, _SCORE, _DISTANCE, _FOUND_MATCH };

	/**
	 * Default fields and additional input fields
	 */
	private String[] fieldNames;

	public BevRestClient() {
		this.options = buildOptions();
	}

	private Options buildOptions() {
		Options options = new Options();

		options.addOption(Option.builder("b").longOpt("batch")
				.desc("Batch processes the file line by line where each line represents an address. "
						+ "The order of addresses may not be preserved.")
				.hasArg().build());
		options.addOption(Option.builder("t").longOpt("threads")
				.desc("The max. number of parallel requests to the ReST endpoint. This defaults to 1.").hasArg()
				.build());
		options.addOption(Option.builder("z").longOpt("postal-code").desc("postal code").hasArg().build());
		options.addOption(Option.builder("p").longOpt("place").desc("place or municipaliy").hasArg().build());
		options.addOption(Option.builder("a").longOpt("address-line").desc("street or building name").hasArg().build());
		options.addOption(Option.builder("i").longOpt("house-id")
				.desc("house id (e.g. 1/2 or 1 Obj. 7) or building name").hasArg().build());
		options.addOption(Option.builder("u").longOpt("unique-only").desc("print only unique results").build());
		options.addOption(Option.builder("s").longOpt("separator").desc("the default field separator is the semi colon")
				.hasArg().build());
		options.addOption(Option.builder("r").longOpt("rest-url").desc("URL to the ReST geocoding service").hasArg()
				.required().argName("URL").build());
		options.addOption(Option.builder("h").longOpt("help").build());

		options.addOption(
				Option.builder().longOpt("radius").hasArg().desc("search radius in km (dot is decimal comma)").build());
		options.addOption(Option.builder().longOpt("longitude").hasArg()
				.desc("longitude of the search center (dot is decimal comma)").build());
		options.addOption(Option.builder().longOpt("latitude").hasArg()
				.desc("latitude of the search center (dot is decimal comma)").build());
		options.addOption(Option.builder().longOpt("disable-certificate-validation")
				.desc("disable the SSL certificate validation").build());
		options.addOption(Option.builder().longOpt("proxy-host").hasArg().desc("the proxy host").build());
		options.addOption(Option.builder().longOpt("proxy-port").hasArg()
				.desc("the proxy port. Only valid in combination with --proxy-host").build());
		options.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("The output is written to this file. The output defaults to the standard output.").build());

		return options;
	}

	/**
	 * Output the query result to the command line
	 * 
	 * @param separator
	 * @param results
	 * @throws IOException
	 */
	protected void outputResults(String separator, List<BevQueryResult> results) throws IOException {
		Map<String, Object> fieldValues = new HashMap<>();
		for (BevQueryResult result : results) {
			fieldValues.put(_POSTAL_CODE, result.getPostalCode());
			fieldValues.put(_PLACE, result.getPlace());
			fieldValues.put(_STREET, result.getStreet());
			fieldValues.put(_HOUSE_NUMBER, result.getHouseNumber());
			fieldValues.put(_HOUSE_NUMBER_ADDITION, result.getHouseNumberAddition());
			fieldValues.put(_BUILDING_ID, result.getBuildingId());
			fieldValues.put(_ADDRESS_NAME, result.getAddressName());
			fieldValues.put(_BUILDING_NAME, result.getBuildingName());
			fieldValues.put(_MUNICIPALITY, result.getMunicipality());
			fieldValues.put(_LONGITUDE, result.getLongitude());
			fieldValues.put(_LATITUDE, result.getLatitude());
			fieldValues.put(_ID, result.getId());
			fieldValues.put(_ADRCD, result.getAdrcd());
			fieldValues.put(_SUBCD, result.getSubcd());
			fieldValues.put(_SKZ, result.getSkz());
			fieldValues.put(_OKZ, result.getOkz());
			fieldValues.put(_GKZ, result.getGkz());
			fieldValues.put(_WARNING, result.getWarning());
			fieldValues.put(_SCORE, result.getScore());
			fieldValues.put(_DISTANCE, result.getDistance());
			fieldValues.put(_FOUND_MATCH, result.getFoundMatch());

			for (int i = 0; i < fieldNames.length; i++) {
				if (fieldValues.containsKey(fieldNames[i]))
					continue;
				fieldValues.put(fieldNames[i], result.getInputData().get(fieldNames[i]));
			}

			csvWriter.write(fieldValues, fieldNames);
		}
	}

	/**
	 * Query the ReST endpoint using the command line arguments
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public void query(String[] args) {
		BevQueryExecutor executor = null;
		ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			int threadPoolSize = 1;
			if (line.hasOption("t")) {
				threadPoolSize = Integer.parseInt(line.getOptionValue("t"));
			}

			String postalCode = null;
			String place = null;
			String addressLine = null;
			String houseId = null;
			String radius = null;
			String longitude = null;
			String latitude = null;
			String separator = ";";
			boolean enforceUnique = false;
			if (line.hasOption("z")) {
				postalCode = line.getOptionValue("z");
			}
			if (line.hasOption("p")) {
				place = line.getOptionValue("p");
			}
			if (line.hasOption("a")) {
				addressLine = line.getOptionValue("a");
			}
			if (line.hasOption("i")) {
				houseId = line.getOptionValue("i");
			}
			if (line.hasOption("radius")) {
				radius = line.getOptionValue("radius");
			}
			if (line.hasOption("longitude")) {
				longitude = line.getOptionValue("longitude");
			}
			if (line.hasOption("latitude")) {
				latitude = line.getOptionValue("latitude");
			}
			if (line.hasOption("s")) {
				separator = line.getOptionValue("s");
			}
			if (line.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar BevRestClient.jar", options, true);
				System.exit(0);
			}
			if (line.hasOption("u")) {
				enforceUnique = true;
			}

			if (line.hasOption("disable-certificate-validation")) {
				clientBuilder.disableTrustManager();
			}

			if (!line.hasOption("proxy-port") && line.hasOption("proxy-host")) {
				throw new ParseException(
						"The option --proxy-host is only allowed in combination with the option --proxy-port.");
			}
			if (line.hasOption("proxy-port") && !line.hasOption("proxy-host")) {
				throw new ParseException(
						"The option --proxy-port is only allowed in combination with the option --proxy-host.");
			}
			if (line.hasOption("proxy-host") && line.hasOption("proxy-port")) {
				clientBuilder.defaultProxy(line.getOptionValue("proxy-host"),
						Integer.parseInt(line.getOptionValue("proxy-port")));
			}

			OutputStreamWriter output;
			if (line.hasOption("o")) {
				output = new OutputStreamWriter(new FileOutputStream(line.getOptionValue("o")));
			} else {
				output = new OutputStreamWriter(System.out);
			}

			// avoid concurrent access exceptions in the Apache http client
			clientBuilder.connectionPoolSize(threadPoolSize);
			executor = new BevQueryExecutor(clientBuilder.build(), line.getOptionValue("r"), threadPoolSize);

			CsvPreference csvPreference = new CsvPreference.Builder('"',
					Objects.toString(line.getOptionValue("s"), ";").toCharArray()[0],
					System.getProperty("line.separator")).build();
			csvWriter = new CsvMapWriter(output, csvPreference, true);

			if (line.hasOption("b")) {
				ICsvMapReader mapReader = null;
				try {
					FileReader fileReader = new FileReader(line.getOptionValue("b"));
					mapReader = new CsvMapReader(fileReader, csvPreference);

					// calculate the output header (field names)
					final String[] header = mapReader.getHeader(true);
					ArrayList<String> tempFields = new ArrayList<>(Arrays.asList(defaultFieldNames));
					for (int i = 0; i < header.length; i++) {
						if (!tempFields.contains(header[i])) {
							tempFields.add(header[i]);
						}
					}
					fieldNames = tempFields.toArray(new String[] {});

					Map<String, String> inputData;
					List<Future<List<BevQueryResult>>> queryResults = new ArrayList<>();
					while ((inputData = mapReader.read(header)) != null) {
						queryResults
								.add(executor.query(inputData.get(INPUT_POSTAL_CODE), inputData.get(INPUT_PLACE),
										inputData.get(INPUT_ADDRESS_LINE), inputData.get(INPUT_HOUSE_ID),
										inputData.get(INPUT_LONGITUDE), inputData.get(INPUT_LATITUDE),
										inputData.get(INPUT_RADIUS),
										inputData.get(INPUT_ENFORCE_UNIQUE) == null ? false
												: Boolean.parseBoolean(inputData.get(INPUT_ENFORCE_UNIQUE)),
										inputData));
					}
					csvWriter.writeHeader(fieldNames);
					for (Future<List<BevQueryResult>> queryResult : queryResults) {
						List<BevQueryResult> results = queryResult.get();
						outputResults(separator, results);
					}
				} finally {
					if (mapReader != null) {
						mapReader.close();
					}
				}
			} else {
				fieldNames = defaultFieldNames;
				Map<String, String> inputData = new HashMap<String, String>();
				Future<List<BevQueryResult>> queryResult = executor.query(postalCode, place, addressLine, houseId,
						longitude, latitude, radius, enforceUnique, inputData);
				try {
					List<BevQueryResult> results = queryResult.get();
					if (enforceUnique && results.size() == 1) {
						if (!results.get(0).getFoundMatch())
							throw new Exception("No unique result found.");
					}
					outputResults(separator, results);
				} catch (ExecutionException e) {
					throw e.getCause();
				}
			}
		} catch (ParseException exp) {
			System.out.println(exp.getMessage());
			System.out.println();
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar BevRestClient.jar", options, true);
			System.exit(-2);
		} catch (BevRestException e) {
			System.err.println(e.toString());
			System.exit(e.getErrorCode());
		} catch (JsonProcessingException e) {
			System.err.println(e.toString());
			System.exit(-3);
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(-4);
		} catch (Throwable t) {
			System.err.println(t.toString());
			t.printStackTrace();
			System.exit(-1);
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
			if (executor != null) {
				executor.dispose();
			}
		}
	}

	public static void main(String[] args) {
		BevRestClient bevClient = new BevRestClient();
		bevClient.query(args);
	}

}
