package at.itbh.bev.utils;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @see <a href="https://github.com/juraj-blahunka/site-examples/tree/master/inject-jboss-system-properties">https://github.com/juraj-blahunka/site-examples/tree/master/inject-jboss-system-properties</a>
 */
public class SystemPropertyProvider {

	@Produces
	@SystemProperty("")
	String findProperty(InjectionPoint ip) {
		SystemProperty annotation = ip.getAnnotated()
				.getAnnotation(SystemProperty.class);

		String name = annotation.value();
		String found = System.getProperty(name);
		if (found == null) {
			throw new IllegalStateException("System property '" + name + "' is not defined!");
		}
		return found;
	}
}