/* 
 * Zaehlsprengel.java
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

package at.itbh.bev.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ZAEHLSPRENGEL database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name="Zaehlsprengel.findAll", query="SELECT z FROM Zaehlsprengel z")
public class Zaehlsprengel implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ZaehlsprengelPK id;

	private String zaehlsprengelname;

	//bi-directional many-to-one association to Adresse
	@OneToMany(mappedBy="zaehlsprengelBean")
	private List<Adresse> adresses;

	public Zaehlsprengel() {
	}

	public ZaehlsprengelPK getId() {
		return this.id;
	}

	public void setId(ZaehlsprengelPK id) {
		this.id = id;
	}

	public String getZaehlsprengelname() {
		return this.zaehlsprengelname;
	}

	public void setZaehlsprengelname(String zaehlsprengelname) {
		this.zaehlsprengelname = zaehlsprengelname;
	}

	public List<Adresse> getAdresses() {
		return this.adresses;
	}

	public void setAdresses(List<Adresse> adresses) {
		this.adresses = adresses;
	}

	public Adresse addAdress(Adresse adress) {
		getAdresses().add(adress);
		adress.setZaehlsprengelBean(this);

		return adress;
	}

	public Adresse removeAdress(Adresse adress) {
		getAdresses().remove(adress);
		adress.setZaehlsprengelBean(null);

		return adress;
	}

}