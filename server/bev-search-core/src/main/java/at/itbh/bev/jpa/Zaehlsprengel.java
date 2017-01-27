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