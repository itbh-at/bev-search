/* 
 * Imports.java
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