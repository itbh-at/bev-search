/* 
 * DataBean.java
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

package at.itbh.bev;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.itbh.bev.jpa.AdresseDenormalized;

@Stateless
public class DataBean {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Query the database for all addresses (<code>BEV.ADRESSE</code>) and
	 * buildings (<code>BEV.GEBAEUDE</code>) with the address code
	 * <code>adrcd</code>
	 * 
	 * <p>
	 * It is important to know that what is commonly referred to as address
	 * corresponds to a combination of address and building in the BEV data set.
	 * There are addresses with buildings and without buildings. The table
	 * <code>BEV.ADRESSE_DENORMALIZED</code> manages this combination and
	 * introduces a new unique key which is not part of the BEV data set.
	 * </p>
	 * 
	 * <p>
	 * <b>It is wrong to expect a unique address record when querying the BEV
	 * data set with an <code>ADRCD</code>. <code>ADRCD</code> my be unique if
	 * there are no buildings. If there are buildings the combination of
	 * <code>ADRCD</code> and <code>SUBCD</code> from table
	 * <code>BEV.GEBAEUDE</code> is a unique identifier.
	 * </p>
	 * 
	 * @param adrcd
	 *            the BEV identifier of an address
	 * @return the list of flattened addresses
	 */
	public List<AdresseDenormalized> getAddressesByADRCD(String adrcd) {
		TypedQuery<AdresseDenormalized> query = em.createNamedQuery("AdresseDenormalized.findByADRCD",
				AdresseDenormalized.class);
		query.setParameter("adrcd", adrcd);
		return query.getResultList();
	}

}
