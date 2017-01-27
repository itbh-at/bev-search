package at.itbh.bev.rest.util;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

import at.itbh.bev.api.BevFinder;
import at.itbh.bev.api.CommonBevFinder;
import at.itbh.bev.api.Util;

/**
 * The Imports class is used to alias EJBs imported from other applications as
 * local CDI beans, thus allowing consumers to ignore the details of
 * inter-application communication.
 * 
 * @see <a href="https://github.com/wildfly/quickstart/tree/10.x/inter-app">https://github.com/wildfly/quickstart/tree/10.x/inter-app</a>
 */
public class Imports {

	@Produces
	@EJB(lookup = "java:global/bev-search-core/CommonBevFinderBean!at.itbh.bev.api.CommonBevFinder")
	private CommonBevFinder simpleFinder;
	
	@Produces
	@EJB(lookup = "java:global/bev-search-core/BevFinderBean!at.itbh.bev.api.BevFinder")
	private BevFinder finder;
	
	@Produces
	@EJB(lookup = "java:global/bev-search-core/UtilBean!at.itbh.bev.api.Util")
	private Util util;

	private Imports() {
		// Disable instantiation of this class
	}

}