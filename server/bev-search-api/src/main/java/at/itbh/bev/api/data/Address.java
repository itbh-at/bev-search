/* 
 * Address.java
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

package at.itbh.bev.api.data;

public interface Address {

	/**
	 * @return the unique identifier of this entity
	 */
	public String getId();

	/**
	 * @return the BEV identifier of the address
	 */
	public String getAdrcd();

	/**
	 * @return in combination with {@link #getAdrcd()} the unique identifier of
	 *         a building belonging to an address. It may be <code>null</code>
	 *         if no building belongs to the address.
	 */
	public String getSubcd();
	
	public String getOkz();
	
	public String getSkz();
	
	public String getGkz();

	public Double getLatitude();
	
	public Double getLongitude();
}