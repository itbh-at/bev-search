package at.itbh.bev.rest.client;

import java.io.IOException;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BevRestClient {

	private Options options;
	private Client client = ClientBuilder.newBuilder().build();
	private CommandLineParser parser = new DefaultParser();

	public BevRestClient() {
		this.options = buildOptions();
	}

	private Options buildOptions() {
		Options options = new Options();

		options.addOption(Option.builder("z").longOpt("postal-code").desc("postal code").hasArg().build());
		options.addOption(Option.builder("p").longOpt("place").desc("place or municipaliy").hasArg().build());
		options.addOption(Option.builder("a").longOpt("address-line").desc("street or building name").hasArg().build());
		options.addOption(Option.builder("i").longOpt("house-id")
				.desc("house id (e.g. 1/2 or 1 Obj. 7) or building name").hasArg().build());
		options.addOption(
				Option.builder("u").longOpt("unique-only").desc("print only unique results").build());
		options.addOption(Option.builder("s").longOpt("separator").desc("the default field separator is the semi colon")
				.hasArg().build());
		options.addOption(Option.builder("r").longOpt("rest-url").desc("URL to the ReST geocoding service").hasArg()
				.required().argName("URL").build());
		options.addOption(Option.builder("h").longOpt("help").build());
		
		options.addOption(Option.builder().longOpt("radius").hasArg().desc("search radius in km (dot is decimal comma)").build());
		options.addOption(Option.builder().longOpt("longitude").hasArg().desc("longitude of the search center (dot is decimal comma)").build());
		options.addOption(Option.builder().longOpt("latitude").hasArg().desc("latitude of the search center (dot is decimal comma)").build());

		return options;
	}

	public void query(String[] args) {
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			WebTarget target = client.target(line.getOptionValue("r"));
			
			String separator = ";";
			boolean unique = false;
			if (line.hasOption("z")) {
				target = target.queryParam("postalCode", line.getOptionValue("z"));
			}
			if (line.hasOption("p")) {
				target = target.queryParam("place", line.getOptionValue("p"));
			}
			if (line.hasOption("a")) {
				target = target.queryParam("addressLine", line.getOptionValue("a"));
			}
			if (line.hasOption("i")) {
				target = target.queryParam("houseId", line.getOptionValue("i"));
			}
			if (line.hasOption("s")) {
				separator = line.getOptionValue("s");
			}
			if (line.hasOption("radius")) {
				target = target.queryParam("radius", line.getOptionValue("radius"));
			}
			if (line.hasOption("longitude")) {
				target = target.queryParam("longitude", line.getOptionValue("longitude"));
			}
			if (line.hasOption("latitude")) {
				target = target.queryParam("latitude", line.getOptionValue("latitude"));
			}
			if (line.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar BevRestClient.jar", options, true);
				System.exit(0);
			}
			if (line.hasOption("u")) {
				unique = true;
			}
			
			// Build a HTTP GET request that accepts "text/plain" response type
			// and contains a custom HTTP header entry "Foo: bar".
			Invocation invocation = target.request(MediaType.APPLICATION_JSON).buildGet();

			// Invoke the request using generic interface
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
				System.err.println(message);
				System.exit(code);
			}
			
			StringBuilder sb = new StringBuilder();			
			Iterator<JsonNode> iter = root.at("/response").elements();
			int i = 0;
			while (iter.hasNext()) {
				JsonNode node = iter.next();
				sb.append("\"");
				sb.append(node.at("/address/postalCode").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/place").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/street").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/houseNumber").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/houseNumberAddition").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/buildingId").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/addressName").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/buildingName").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/municipality").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append(node.at("/address/longitude").asText(""));
				sb.append(separator);
				sb.append(node.at("/address/latitude").asText(""));
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/id").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/adrcd").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/subcd").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/skz").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/okz").asText(""));
				sb.append("\"");
				sb.append(separator);
				
				sb.append("\"");
				sb.append(node.at("/address/gkz").asText(""));
				sb.append("\"");
				sb.append(separator);

				sb.append(node.at("/score").asText(""));
				sb.append(separator);
				sb.append(node.at("/distance").asText(""));
				sb.append(separator);
				sb.append(System.getProperty("line.separator"));
				i++;
			}
			
			if (unique && i > 1) {
				System.exit(-5);
			}
			System.out.println(sb.toString());
		} 
		catch (ParseException exp) {
			System.out.println(exp.getMessage());
			System.out.println();
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar BevRestClient.jar", options, true);
			System.exit(-2);
		} catch (JsonProcessingException e) {
			System.err.println(e.toString());
			System.exit(-3);
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(-4);
		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		BevRestClient bevClient = new BevRestClient();
		bevClient.query(args);
	}

}
