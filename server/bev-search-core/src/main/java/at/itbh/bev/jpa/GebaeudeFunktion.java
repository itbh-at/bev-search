/* 
 * GebaeudeFunktion.java
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
 * The persistent class for the GEBAEUDE_FUNKTION database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@Table(name="GEBAEUDE_FUNKTION")
@NamedQuery(name="GebaeudeFunktion.findAll", query="SELECT g FROM GebaeudeFunktion g")
public class GebaeudeFunktion implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GebaeudeFunktionPK id;
	
	@Column(insertable=false, updatable=false)
	private String objektnummer;

	@Column(insertable=false, updatable=false)
	private String objfunktkennziffer;
	
	private String adrcd;
	
	private String subcd;

	//bi-directional one-to-one association to Gebaeude
	@OneToOne(mappedBy="gebaeudeFunktion", fetch=FetchType.LAZY)
	private Gebaeude gebaeude;

	public GebaeudeFunktion() {
	}

	public String getObjektnummer() {
		return this.objektnummer;
	}

	public void setObjektnummer(String objektnummer) {
		this.objektnummer = objektnummer;
	}

	public String getObjfunktkennziffer() {
		return this.objfunktkennziffer;
	}

	public void setObjfunktkennziffer(String objfunktkennziffer) {
		this.objfunktkennziffer = objfunktkennziffer;
	}

	public Gebaeude getGebaeude() {
		return this.gebaeude;
	}

	public void setGebaeude(Gebaeude gebaeude) {
		this.gebaeude = gebaeude;
	}

	public String getAdrcd() {
		return adrcd;
	}

	public void setAdrcd(String adrcd) {
		this.adrcd = adrcd;
	}

	public String getSubcd() {
		return subcd;
	}

	public void setSubcd(String subcd) {
		this.subcd = subcd;
	}

}