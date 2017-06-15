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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class BevRestClient {

	private Options options;
	private Client client;
	private CommandLineParser parser = new DefaultParser();
	private ICsvMapWriter csvWriter = null;

	public BevRestClient() {
		this.options = buildOptions();
	}

	private Options buildOptions() {
		Options options = new Options();

		options.addOption(Option.builder("b").longOpt("batch")
				.desc("Batch processes the file containing addresses. "
						+ "Each input line is written to stdout and the matching address is appended if the match is unique. "
						+ "A column is appendend which contains `true` if the matching address differs significantly form the input. "
						+ "The order of addresses may not be preserved.")
				.hasArg().build());
		options.addOption(Option.builder("t").longOpt("threads")
				.desc("The max. number of parallel requests to the ReST endpoint. This defaults to 10.").hasArg()
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
		options.addOption(Option.builder().longOpt("proxyHost").hasArg().desc("the proxy host").build());
		options.addOption(Option.builder().longOpt("proxyPort").hasArg().desc("the proxy port. Only valid in combination with proxyHost").build());

		return options;
	}

	protected void outputResults(String separator, List<BevQueryResult> results) {
		// TODO use CSVMapWriter
		for (BevQueryResult result : results) {
			StringBuilder sb = new StringBuilder();
			sb.append("\"");
			sb.append(result.getPostalCode());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getPlace());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getStreet());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getHouseNumber());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getHouseNumberAddition());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getBuildingId());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getAddressName());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getBuildingName());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getMunicipality());
			sb.append("\"");
			sb.append(separator);

			sb.append(result.getLongitude());
			sb.append(separator);
			sb.append(result.getLatitude());
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getId());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getAdrcd());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getSubcd());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getSkz());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getOkz());
			sb.append("\"");
			sb.append(separator);

			sb.append("\"");
			sb.append(result.getGkz());
			sb.append("\"");
			sb.append(separator);

			sb.append(result.getWarning());
			sb.append(separator);

			sb.append(result.getScore());
			sb.append(separator);
			sb.append(result.getDistance());
			sb.append(separator);
			sb.append(System.getProperty("line.separator"));

			System.out.println(sb.toString());
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
			
			if (!line.hasOption("proxyPort") && line.hasOption("proxyHost")) {
				throw new ParseException(
						"The option proxyHost is only allowed in combination with the option proxyPort.");
			}
			if (line.hasOption("proxyPort") && !line.hasOption("proxyHost")) {
				throw new ParseException(
						"The option proxyPort is only allowed in combination with the option proxyHost.");
			}
			 if (line.hasOption("proxyHost") && line.hasOption("proxyPort")) {
				clientBuilder.defaultProxy(line.getOptionValue("proxyHost"),
						Integer.parseInt(line.getOptionValue("proxyPort")));
			}
			
			client = clientBuilder.build();
			WebTarget target = client.target(line.getOptionValue("r"));
			executor = new BevQueryExecutor(target, threadPoolSize);
			
			CsvPreference csvPreference = new CsvPreference.Builder('"',
					Objects.toString(line.getOptionValue("s"), ";").toCharArray()[0],
					System.getProperty("line.separator")).build();
			csvWriter = new CsvMapWriter(new OutputStreamWriter(System.out), csvPreference, true);

			if (line.hasOption("b")) {

			} else {
				Future<List<BevQueryResult>> queryResult = executor.query(postalCode, place, addressLine, houseId,
						longitude, latitude, radius, enforceUnique);
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
