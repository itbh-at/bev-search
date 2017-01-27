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