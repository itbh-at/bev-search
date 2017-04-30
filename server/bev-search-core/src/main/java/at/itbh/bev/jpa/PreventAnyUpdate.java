/* 
 * PreventAnyUpdate.java
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
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PreventAnyUpdate {

	@PrePersist
	void onPrePersists(Object o) {
		throw new RuntimeException("Persisting an entity is strictly forbidden.");
	}
	
	@PreUpdate
	void onPreUpdate(Object o) {
		throw new RuntimeException("Updating an entity is strictly forbidden.");
	}
	
	@PreRemove
	void onPreRemove(Object o) {
		throw new RuntimeException("Removing an entity is strictly forbidden.");
	}
}