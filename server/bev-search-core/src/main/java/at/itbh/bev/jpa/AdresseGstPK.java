/* 
 * AdresseGstPK.java
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