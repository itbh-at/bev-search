package at.itbh.bev.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the GEBAEUDE database table.
 * 
 */
@Embeddable
public class GebaeudePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String adrcd;

	private String subcd;

	public GebaeudePK() {
	}
	public String getAdrcd() {
		return this.adrcd;
	}
	public void setAdrcd(String adrcd) {
		this.adrcd = adrcd;
	}
	public String getSubcd() {
		return this.subcd;
	}
	public void setSubcd(String subcd) {
		this.subcd = subcd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GebaeudePK)) {
			return false;
		}
		GebaeudePK castOther = (GebaeudePK)other;
		return 
			this.adrcd.equals(castOther.adrcd)
			&& this.subcd.equals(castOther.subcd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.adrcd.hashCode();
		hash = hash * prime + this.subcd.hashCode();
		
		return hash;
	}
}