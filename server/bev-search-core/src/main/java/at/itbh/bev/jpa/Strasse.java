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