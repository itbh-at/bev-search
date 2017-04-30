/* 
 * Ortschaft.java
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
 * The persistent class for the ORTSCHAFT database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name="Ortschaft.findAll", query="SELECT o FROM Ortschaft o")
public class Ortschaft implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String okz;

	private String ortsname;
	
	private String gkz;

	//bi-directional many-to-one association to Adresse
	@OneToMany(mappedBy="ortschaft")
	private List<Adresse> adresses;

	public Ortschaft() {
	}
	
	public String getGkz() {
		return gkz;
	}

	public void setGkz(String gkz) {
		this.gkz = gkz;
	}

	public String getOkz() {
		return this.okz;
	}

	public void setOkz(String okz) {
		this.okz = okz;
	}

	public String getOrtsname() {
		return this.ortsname;
	}

	public void setOrtsname(String ortsname) {
		this.ortsname = ortsname;
	}

	public List<Adresse> getAdresses() {
		return this.adresses;
	}

	public void setAdresses(List<Adresse> adresses) {
		this.adresses = adresses;
	}

	public Adresse addAdress(Adresse adress) {
		getAdresses().add(adress);
		adress.setOrtschaft(this);

		return adress;
	}

	public Adresse removeAdress(Adresse adress) {
		getAdresses().remove(adress);
		adress.setOrtschaft(null);

		return adress;
	}

}