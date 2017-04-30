/* 
 * GebaeudeFunktionPK.java
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