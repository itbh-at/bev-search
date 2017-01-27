package at.itbh.bev.utils;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

/**
 *
 * @author DI Christoph D. Hermann, ITBH
 *         <a href="mailto:christoph.hermann@itbh.at">&lt;christoph.hermann@itbh
 *         .at&gt;</a>
 *
 */
public class Logging {

	/**
	 * Creates a {@link Logger} named after the declaring class of the object it
	 * is injected into.
	 * 
	 * @param injectionPoint
	 * @return a configured {@link Logger}
	 */
	@Produces
	Logger createLogger(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

}