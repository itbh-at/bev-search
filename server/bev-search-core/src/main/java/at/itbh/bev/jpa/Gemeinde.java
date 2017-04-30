/* 
 * Gemeinde.java
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
 * The persistent class for the GEMEINDE database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name="Gemeinde.findAll", query="SELECT g FROM Gemeinde g")
public class Gemeinde implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String gkz;

	private String gemeindename;

	//bi-directional many-to-one association to Adresse
	@OneToMany(mappedBy="gemeinde")
	private List<Adresse> adresses;

	public Gemeinde() {
	}

	public String getGkz() {
		return this.gkz;
	}

	public void setGkz(String gkz) {
		this.gkz = gkz;
	}

	public String getGemeindename() {
		return this.gemeindename;
	}

	public void setGemeindename(String gemeindename) {
		this.gemeindename = gemeindename;
	}

	public List<Adresse> getAdresses() {
		return this.adresses;
	}

	public void setAdresses(List<Adresse> adresses) {
		this.adresses = adresses;
	}

	public Adresse addAdress(Adresse adress) {
		getAdresses().add(adress);
		adress.setGemeinde(this);

		return adress;
	}

	public Adresse removeAdress(Adresse adress) {
		getAdresses().remove(adress);
		adress.setGemeinde(null);

		return adress;
	}

}