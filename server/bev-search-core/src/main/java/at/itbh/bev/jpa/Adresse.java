/* 
 * Adresse.java
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
 * The persistent class for the ADRESSE database table.
 * 
 */
@Entity
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name="Adresse.findAll", query="SELECT a FROM Adresse a")
public class Adresse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String adrcd;

	private String bestimmungsart;

	private String epsg;

	private String gnradresse;

	private String hausnrbereich;

	private String hausnrbuchstabe1;

	private String hausnrbuchstabe2;

	private String hausnrtext;

	private String hausnrverbindung1;

	private String hausnrzahl1;

	private String hausnrzahl2;

	private String hofname;

	private String hw;

	private String plz;

	private String quelladresse;

	private String rw;

	//bi-directional many-to-one association to Gemeinde
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GKZ")
	private Gemeinde gemeinde;

	//bi-directional many-to-one association to Ortschaft
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OKZ", referencedColumnName="GKZ")
	private Ortschaft ortschaft;

	//bi-directional many-to-one association to Strasse
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SKZ")
	private Strasse strasse;

	//bi-directional many-to-one association to Zaehlsprengel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="ADRCD", referencedColumnName="GKZ"),
		@JoinColumn(name="ZAEHLSPRENGEL", referencedColumnName="ZAEHLSPRENGEL")
		})
	private Zaehlsprengel zaehlsprengelBean;

	//bi-directional many-to-one association to AdresseGst
	@OneToMany(mappedBy="adresse")
	private List<AdresseGst> adresseGsts;

	//bi-directional many-to-one association to Gebaeude
	@OneToMany(mappedBy="adresse")
	private List<Gebaeude> gebaeudes;

	public Adresse() {
	}

	public String getAdrcd() {
		return this.adrcd;
	}

	public void setAdrcd(String adrcd) {
		this.adrcd = adrcd;
	}

	public String getBestimmungsart() {
		return this.bestimmungsart;
	}

	public void setBestimmungsart(String bestimmungsart) {
		this.bestimmungsart = bestimmungsart;
	}

	public String getEpsg() {
		return this.epsg;
	}

	public void setEpsg(String epsg) {
		this.epsg = epsg;
	}

	public String getGnradresse() {
		return this.gnradresse;
	}

	public void setGnradresse(String gnradresse) {
		this.gnradresse = gnradresse;
	}

	public String getHausnrbereich() {
		return this.hausnrbereich;
	}

	public void setHausnrbereich(String hausnrbereich) {
		this.hausnrbereich = hausnrbereich;
	}

	public String getHausnrbuchstabe1() {
		return this.hausnrbuchstabe1;
	}

	public void setHausnrbuchstabe1(String hausnrbuchstabe1) {
		this.hausnrbuchstabe1 = hausnrbuchstabe1;
	}

	public String getHausnrbuchstabe2() {
		return this.hausnrbuchstabe2;
	}

	public void setHausnrbuchstabe2(String hausnrbuchstabe2) {
		this.hausnrbuchstabe2 = hausnrbuchstabe2;
	}

	public String getHausnrtext() {
		return this.hausnrtext;
	}

	public void setHausnrtext(String hausnrtext) {
		this.hausnrtext = hausnrtext;
	}

	public String getHausnrverbindung1() {
		return this.hausnrverbindung1;
	}

	public void setHausnrverbindung1(String hausnrverbindung1) {
		this.hausnrverbindung1 = hausnrverbindung1;
	}

	public String getHausnrzahl1() {
		return this.hausnrzahl1;
	}

	public void setHausnrzahl1(String hausnrzahl1) {
		this.hausnrzahl1 = hausnrzahl1;
	}

	public String getHausnrzahl2() {
		return this.hausnrzahl2;
	}

	public void setHausnrzahl2(String hausnrzahl2) {
		this.hausnrzahl2 = hausnrzahl2;
	}

	public String getHofname() {
		return this.hofname;
	}

	public void setHofname(String hofname) {
		this.hofname = hofname;
	}

	public String getHw() {
		return this.hw;
	}

	public void setHw(String hw) {
		this.hw = hw;
	}

	public String getPlz() {
		return this.plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
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

	public Gemeinde getGemeinde() {
		return this.gemeinde;
	}

	public void setGemeinde(Gemeinde gemeinde) {
		this.gemeinde = gemeinde;
	}

	public Ortschaft getOrtschaft() {
		return this.ortschaft;
	}

	public void setOrtschaft(Ortschaft ortschaft) {
		this.ortschaft = ortschaft;
	}

	public Strasse getStrasse() {
		return this.strasse;
	}

	public void setStrasse(Strasse strasse) {
		this.strasse = strasse;
	}

	public Zaehlsprengel getZaehlsprengelBean() {
		return this.zaehlsprengelBean;
	}

	public void setZaehlsprengelBean(Zaehlsprengel zaehlsprengelBean) {
		this.zaehlsprengelBean = zaehlsprengelBean;
	}

	public List<AdresseGst> getAdresseGsts() {
		return this.adresseGsts;
	}

	public void setAdresseGsts(List<AdresseGst> adresseGsts) {
		this.adresseGsts = adresseGsts;
	}

	public AdresseGst addAdresseGst(AdresseGst adresseGst) {
		getAdresseGsts().add(adresseGst);
		adresseGst.setAdresse(this);

		return adresseGst;
	}

	public AdresseGst removeAdresseGst(AdresseGst adresseGst) {
		getAdresseGsts().remove(adresseGst);
		adresseGst.setAdresse(null);

		return adresseGst;
	}

	public List<Gebaeude> getGebaeudes() {
		return this.gebaeudes;
	}

	public void setGebaeudes(List<Gebaeude> gebaeudes) {
		this.gebaeudes = gebaeudes;
	}

	public Gebaeude addGebaeude(Gebaeude gebaeude) {
		getGebaeudes().add(gebaeude);
		gebaeude.setAdresse(this);

		return gebaeude;
	}

	public Gebaeude removeGebaeude(Gebaeude gebaeude) {
		getGebaeudes().remove(gebaeude);
		gebaeude.setAdresse(null);

		return gebaeude;
	}

}