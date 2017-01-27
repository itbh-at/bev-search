package at.itbh.bev.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * All ReST services are provided in the root path
 * <code>deyployment-url/rest</code>.
 * 
 * @author Christoph D. Hermann (ITBH) <christoph.hermann@itbh.at>
 *
 */
@ApplicationPath("/v1/at")
public class JaxRsActivator extends Application {

}