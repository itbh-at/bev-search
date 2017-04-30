/* 
 * GebaeudePK.java
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