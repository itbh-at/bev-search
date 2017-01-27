package at.itbh.bev.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ADRESSE_GST database table.
 * 
 */
@Embeddable
public class AdresseGstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
	private String gstnr;

	private String kgnr;

	private String lfdnr;
	
	public AdresseGstPK() {
	}

	public String getGstnr() {
		return gstnr;
	}

	public String getKgnr() {
		return kgnr;
	}

	public String getLfdnr() {
		return lfdnr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gstnr == null) ? 0 : gstnr.hashCode());
		result = prime * result + ((kgnr == null) ? 0 : kgnr.hashCode());
		result = prime * result + ((lfdnr == null) ? 0 : lfdnr.hashCode());
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
		AdresseGstPK other = (AdresseGstPK) obj;
		if (gstnr == null) {
			if (other.gstnr != null)
				return false;
		} else if (!gstnr.equals(other.gstnr))
			return false;
		if (kgnr == null) {
			if (other.kgnr != null)
				return false;
		} else if (!kgnr.equals(other.kgnr))
			return false;
		if (lfdnr == null) {
			if (other.lfdnr != null)
				return false;
		} else if (!lfdnr.equals(other.lfdnr))
			return false;
		return true;
	}
	
}