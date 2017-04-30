/* 
 * SystemPropertyProvider.java
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