/* 
 * AdresseGst.java
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


/**
 * The persistent class for the ADRESSE_GST database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@Table(name="ADRESSE_GST")
@NamedQuery(name="AdresseGst.findAll", query="SELECT a FROM AdresseGst a")
public class AdresseGst implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AdresseGstPK id;
	
	@Column(insertable=false, updatable=false)
	private String gstnr;

	@Column(insertable=false, updatable=false)
	private String kgnr;

	@Column(insertable=false, updatable=false)
	private String lfdnr;

	//bi-directional many-to-one association to Adresse
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADRCD")
	private Adresse adresse;

	public AdresseGst() {
	}

	public String getGstnr() {
		return this.gstnr;
	}

	public void setGstnr(String gstnr) {
		this.gstnr = gstnr;
	}

	public String getKgnr() {
		return this.kgnr;
	}

	public void setKgnr(String kgnr) {
		this.kgnr = kgnr;
	}

	public String getLfdnr() {
		return this.lfdnr;
	}

	public void setLfdnr(String lfdnr) {
		this.lfdnr = lfdnr;
	}

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

}