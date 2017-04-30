/* 
 * QueryResult.java
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

package at.itbh.bev.api.data;

/**
 * A single result from a Lucene query
 */
public abstract class QueryResult {

	private Float score = 0f;

	private Double distance;
	
	/**
	 * The higher the better the match
	 * 
	 * @return the Lucene matching score
	 */
	public Float getScore() {
		return score;
	}

	/**
	 * Distance of the query result to the provided coordinates.
	 * 
	 * @return the distance or <code>null</code> if not distance has been computed
	 */
	public Double getDistance() {
		return distance;
	}
	
	public void setScore(Float score) {
		this.score = score;
	}
	
	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
