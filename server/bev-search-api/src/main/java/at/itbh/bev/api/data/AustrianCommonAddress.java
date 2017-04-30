/* 
 * AustrianCommonAdress.java
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

/**
 * An address structured in a way commonly used in Austria by people and the
 * postal services. The municipality is also provided because place and
 * municipality are often used interchangeable in every day use.
 * 
 * <p>
 * {@link AustrianCommonAddress} goes online down to the building level. It never
 * contains apartment information.
 * </p>
 */
public interface AustrianCommonAddress extends Address {

	public String getPostalCode();

	public String getPlace();

	public String getMunicipality();

	public String getStreet();

	/**
	 * A text identifying the building
	 * 
	 * @return <code>HAUSNRGEBAEUDEBEZ</code> or <code>null</code>
	 */
	public String getBuildingName();
	
	/**
	 * A text identifying the address
	 * 
	 * @return <code>HOFNAME</code> or <code>null</code>
	 */
	public String getAddressName();

	/**
	 * @return the house number or <code>null</code> if no numeric
	 *         representation is available
	 */
	public Integer getHouseNumber();

	/**
	 * @return the non numeric part of the house number or <code>null</code> if
	 *         not available
	 */
	public String getHouseNumberAddition();

	/**
	 * @return in most cases the staircase but also other building object
	 *         identifiers are possible values (e.g. "Obj. 7")
	 */
	public String getBuildingId();
}