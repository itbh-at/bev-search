/* 
 * Util.java
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

package at.itbh.bev.api;

import java.util.concurrent.Future;

import javax.ejb.Local;

/**
 * Utility methods
 */
@Local
public interface Util {

	/**
	 * Import data from the BEV CSV files and update all internal caches.
	 * 
	 * @return the duration of the import in ms
	 */
	public Future<Long> refreshBevData();

}
