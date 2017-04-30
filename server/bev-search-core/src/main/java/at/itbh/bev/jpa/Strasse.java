/* 
 * Strasse.java
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
 * The persistent class for the STRASSE database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name="Strasse.findAll", query="SELECT s FROM Strasse s")
public class Strasse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String skz;

	private String gkz;

	private String strassenname;

	private String strassennamenzusatz;

	private String szusadrbest;

	//bi-directional many-to-one association to Adresse
	@OneToMany(mappedBy="strasse")
	private List<Adresse> adresses;

	public Strasse() {
	}

	public String getSkz() {
		return this.skz;
	}

	public void setSkz(String skz) {
		this.skz = skz;
	}

	public String getGkz() {
		return this.gkz;
	}

	public void setGkz(String gkz) {
		this.gkz = gkz;
	}

	public String getStrassenname() {
		return this.strassenname;
	}

	public void setStrassenname(String strassenname) {
		this.strassenname = strassenname;
	}

	public String getStrassennamenzusatz() {
		return this.strassennamenzusatz;
	}

	public void setStrassennamenzusatz(String strassennamenzusatz) {
		this.strassennamenzusatz = strassennamenzusatz;
	}

	public String getSzusadrbest() {
		return this.szusadrbest;
	}

	public void setSzusadrbest(String szusadrbest) {
		this.szusadrbest = szusadrbest;
	}

	public List<Adresse> getAdresses() {
		return this.adresses;
	}

	public void setAdresses(List<Adresse> adresses) {
		this.adresses = adresses;
	}

	public Adresse addAdress(Adresse adress) {
		getAdresses().add(adress);
		adress.setStrasse(this);

		return adress;
	}

	public Adresse removeAdress(Adresse adress) {
		getAdresses().remove(adress);
		adress.setStrasse(null);

		return adress;
	}

}