package at.itbh.bev.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ZAEHLSPRENGEL database table.
 * 
 */
@Embeddable
public class ZaehlsprengelPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String gkz;

	private String zaehlsprengel;

	public ZaehlsprengelPK() {
	}
	public String getGkz() {
		return this.gkz;
	}
	public void setGkz(String gkz) {
		this.gkz = gkz;
	}
	public String getZaehlsprengel() {
		return this.zaehlsprengel;
	}
	public void setZaehlsprengel(String zaehlsprengel) {
		this.zaehlsprengel = zaehlsprengel;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ZaehlsprengelPK)) {
			return false;
		}
		ZaehlsprengelPK castOther = (ZaehlsprengelPK)other;
		return 
			this.gkz.equals(castOther.gkz)
			&& this.zaehlsprengel.equals(castOther.zaehlsprengel);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.gkz.hashCode();
		hash = hash * prime + this.zaehlsprengel.hashCode();
		
		return hash;
	}
}