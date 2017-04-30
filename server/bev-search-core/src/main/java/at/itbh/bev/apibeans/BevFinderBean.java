/* 
 * BevFinderBean.java
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

package at.itbh.bev.apibeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.DistanceSortField;
import org.jboss.logging.Logger;

import at.itbh.bev.api.BevFinder;
import at.itbh.bev.api.data.BevAddress;
import at.itbh.bev.api.data.BevQueryResult;
import at.itbh.bev.api.data.GeocodingResult;
import at.itbh.bev.api.exceptions.InvalidApiUsageException;
import at.itbh.bev.api.exceptions.TooManyResultsException;
import at.itbh.bev.jpa.AdresseDenormalized;
import at.itbh.bev.utils.SystemProperty;

@Stateless
public class BevFinderBean implements BevFinder {

	@PersistenceContext
	EntityManager em;

	@Inject
	Logger logger;
	
	@Inject
	FinderImpl finder;

	Integer maxResults = 10;

	boolean prune = false;

	@Inject
	@SystemProperty("at.itbh.bev.results.max")
	String maxResultProperty;

	@Inject
	@SystemProperty("at.itbh.bev.results.prune")
	String pruneProperty;

	@PostConstruct
	protected void init() {
		this.maxResults = Integer.parseInt(maxResultProperty);
		if (pruneProperty.equals("true")) {
			this.prune = true;
		}
	}

	

	@Override
	public GeocodingResult<BevQueryResult> geocode(String postalCode, String place, String addressLine, String houseId)
			throws InvalidApiUsageException {
		FullTextQuery fullTextQuery = finder.constructQuery(em, postalCode, place, addressLine, houseId);

		// only allow results having a municipality or place resembling the
		// provided one
		if (place != null && place.length() > 0) {
			fullTextQuery.enableFullTextFilter("placeFilter").setParameter("place", place).setParameter("entityManager",
					em);
		}

		try {
			return finder.queryIndex(BevQueryResult.class, fullTextQuery, maxResults, prune, postalCode, place, addressLine, houseId, null, null,
					null);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	@Override
	public GeocodingResult<BevQueryResult> reverseGeocode(Double latitude, Double longitude, Float radius)
			throws InvalidApiUsageException {
		if (latitude == null && longitude == null && radius == null) {
			throw new InvalidApiUsageException("Latitude, longitude and radius must be provided.");
		}

		FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
		QueryBuilder b = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(AdresseDenormalized.class).get();
		Query query = b.spatial().onField("location").within(radius, Unit.KM).ofLatitude(latitude)
				.andLongitude(longitude).createQuery();

		FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, AdresseDenormalized.class);
		Sort distanceSort = new Sort(new DistanceSortField(latitude, longitude, "location"));
		fullTextQuery.setSort(distanceSort);

		try {
			return finder.queryIndex(BevQueryResult.class, fullTextQuery, maxResults, prune, null, null, null, null, latitude, longitude, radius);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	@Override
	public List<BevAddress> findAddressByADRCD(String adrcd) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BevAddress findAddress(String adrcd, String subcd) throws TooManyResultsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public BevAddress findAddress(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GeocodingResult<BevQueryResult> geocode(String postalCode, String place, String addressLine, String houseId,
			Double latitude, Double longitude) throws InvalidApiUsageException {
		return geocode(postalCode, place, addressLine, houseId, latitude, longitude, 1f);
	}

	@Override
	public GeocodingResult<BevQueryResult> geocode(String postalCode, String place, String addressLine, String houseId,
			Double latitude, Double longitude, Float radius) throws InvalidApiUsageException {
		FullTextQuery fullTextQuery = finder.constructQuery(em, postalCode, place, addressLine, houseId);

		if (place != null && place.length() > 0) {
			fullTextQuery.enableFullTextFilter("placeFilter").setParameter("place", place).setParameter("entityManager",
					em);
		}

		fullTextQuery.enableFullTextFilter("regionFilter").setParameter("latitude", latitude)
				.setParameter("longitude", longitude).setParameter("radius", radius).setParameter("entityManager", em);

		try {
			return finder.queryIndex(BevQueryResult.class, fullTextQuery, maxResults, prune, postalCode, place, addressLine, houseId, latitude, longitude, radius);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}


}
