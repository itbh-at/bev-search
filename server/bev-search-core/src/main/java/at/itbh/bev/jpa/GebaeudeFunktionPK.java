package at.itbh.bev.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the GEBAEUDE_FUNKTION database table.
 * 
 */
@Embeddable
public class GebaeudeFunktionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String objektnummer;

	private String objfunktkennziffer;
	
	public GebaeudeFunktionPK() {
	}

	public String getObjektnummer() {
		return objektnummer;
	}

	public String getObjfunktkennziffer() {
		return objfunktkennziffer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objektnummer == null) ? 0 : objektnummer.hashCode());
		result = prime * result + ((objfunktkennziffer == null) ? 0 : objfunktkennziffer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GebaeudeFunktionPK other = (GebaeudeFunktionPK) obj;
		if (objektnummer == null) {
			if (other.objektnummer != null)
				return false;
		} else if (!objektnummer.equals(other.objektnummer))
			return false;
		if (objfunktkennziffer == null) {
			if (other.objfunktkennziffer != null)
				return false;
		} else if (!objfunktkennziffer.equals(other.objfunktkennziffer))
			return false;
		return true;
	}
}