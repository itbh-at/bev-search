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