package at.itbh.bev.rest;

import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.itbh.bev.api.Util;

@Path("/")
public class Refresher {

	@Inject
	Util util;
	
	@GET
	@Path("/refresh")
	@Produces(MediaType.APPLICATION_JSON)
	public Future<Long> refresh() {
		return util.refreshBevData();
	}
}
