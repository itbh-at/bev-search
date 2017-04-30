/* 
 * Gebaeude.java
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
 * The persistent class for the GEBAEUDE database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name="Gebaeude.findAll", query="SELECT g FROM Gebaeude g")
public class Gebaeude implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GebaeudePK id;

	private String bestimmungsart;

	private String eigenschaft;

	private String epsg;

	private String hauptadresse;

	private String hausnrbuchstabe3;

	private String hausnrbuchstabe4;

	private String hausnrgebaeudebez;

	private String hausnrverbindung2;

	private String hausnrverbindung3;

	private String hausnrzahl3;

	private String hausnrzahl4;

	private String hw;

	private String objektnummer;

	private String quelladresse;

	private String rw;

	//bi-directional many-to-one association to Adresse
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADRCD", insertable=false, updatable=false)
	private Adresse adresse;

	//bi-directional one-to-one association to GebaeudeFunktion
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ADRCD", referencedColumnName="ADRCD", insertable=false, updatable=false),
		@JoinColumn(name="SUBCD", referencedColumnName="SUBCD", insertable=false, updatable=false)
		})
	private GebaeudeFunktion gebaeudeFunktion;

	public Gebaeude() {
	}

	public GebaeudePK getId() {
		return this.id;
	}

	public void setId(GebaeudePK id) {
		this.id = id;
	}

	public String getBestimmungsart() {
		return this.bestimmungsart;
	}

	public void setBestimmungsart(String bestimmungsart) {
		this.bestimmungsart = bestimmungsart;
	}

	public String getEigenschaft() {
		return this.eigenschaft;
	}

	public void setEigenschaft(String eigenschaft) {
		this.eigenschaft = eigenschaft;
	}

	public String getEpsg() {
		return this.epsg;
	}

	public void setEpsg(String epsg) {
		this.epsg = epsg;
	}

	public String getHauptadresse() {
		return this.hauptadresse;
	}

	public void setHauptadresse(String hauptadresse) {
		this.hauptadresse = hauptadresse;
	}

	public String getHausnrbuchstabe3() {
		return this.hausnrbuchstabe3;
	}

	public void setHausnrbuchstabe3(String hausnrbuchstabe3) {
		this.hausnrbuchstabe3 = hausnrbuchstabe3;
	}

	public String getHausnrbuchstabe4() {
		return this.hausnrbuchstabe4;
	}

	public void setHausnrbuchstabe4(String hausnrbuchstabe4) {
		this.hausnrbuchstabe4 = hausnrbuchstabe4;
	}

	public String getHausnrgebaeudebez() {
		return this.hausnrgebaeudebez;
	}

	public void setHausnrgebaeudebez(String hausnrgebaeudebez) {
		this.hausnrgebaeudebez = hausnrgebaeudebez;
	}

	public String getHausnrverbindung2() {
		return this.hausnrverbindung2;
	}

	public void setHausnrverbindung2(String hausnrverbindung2) {
		this.hausnrverbindung2 = hausnrverbindung2;
	}

	public String getHausnrverbindung3() {
		return this.hausnrverbindung3;
	}

	public void setHausnrverbindung3(String hausnrverbindung3) {
		this.hausnrverbindung3 = hausnrverbindung3;
	}

	public String getHausnrzahl3() {
		return this.hausnrzahl3;
	}

	public void setHausnrzahl3(String hausnrzahl3) {
		this.hausnrzahl3 = hausnrzahl3;
	}

	public String getHausnrzahl4() {
		return this.hausnrzahl4;
	}

	public void setHausnrzahl4(String hausnrzahl4) {
		this.hausnrzahl4 = hausnrzahl4;
	}

	public String getHw() {
		return this.hw;
	}

	public void setHw(String hw) {
		this.hw = hw;
	}

	public String getObjektnummer() {
		return this.objektnummer;
	}

	public void setObjektnummer(String objektnummer) {
		this.objektnummer = objektnummer;
	}

	public String getQuelladresse() {
		return this.quelladresse;
	}

	public void setQuelladresse(String quelladresse) {
		this.quelladresse = quelladresse;
	}

	public String getRw() {
		return this.rw;
	}

	public void setRw(String rw) {
		this.rw = rw;
	}

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public GebaeudeFunktion getGebaeudeFunktion() {
		return this.gebaeudeFunktion;
	}

	public void setGebaeudeFunktion(GebaeudeFunktion gebaeudeFunktion) {
		this.gebaeudeFunktion = gebaeudeFunktion;
	}

}