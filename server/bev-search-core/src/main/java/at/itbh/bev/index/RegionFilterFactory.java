/* 
 * RegionFilterFactory.java
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

package at.itbh.bev.index;

import javax.persistence.EntityManager;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;

import at.itbh.bev.jpa.AdresseDenormalized;

/**
 * Restrict an index query to a certain geographic location
 * 
 * <p>
 * If no radius is set it will default to 1 km.
 * </p>
 */
public class RegionFilterFactory {

	private EntityManager entityManager;

	private Double latitude;

	private Double longitude;

	private Float radius = 1f;

	/**
	 * @param latitude latitude of the search center
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @param longitude longitude of the search center
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Restrict the search to this radius around a search center defined by
	 * {@link #setLatitude(Double)} and {@link #setLongitude(Double)}.
	 * 
	 * @param radius
	 *            in km
	 */
	public void setRadius(Float radius) {
		this.radius = radius;
	}

	/**
	 * This {@link EntityManager} is used to construct a
	 * {@link FullTextEntityManager} and from that a {@link QueryBuilder} to
	 * ensure that the same analyzers as in the indexing phase are used for the
	 * filtering.
	 */
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

	@Factory
	public Filter getFilter() {
		FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(entityManager);
		QueryBuilder b = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(AdresseDenormalized.class).get();

		Query query = b.spatial()
				.onField("location")
				.within(radius, Unit.KM)
				.ofLatitude(latitude)
				.andLongitude(longitude)
				.createQuery();
		
		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
}