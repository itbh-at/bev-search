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