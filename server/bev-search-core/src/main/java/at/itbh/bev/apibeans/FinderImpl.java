/* 
 * FinderImpl.java
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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import at.itbh.bev.api.data.AustrianCommonQueryResult;
import at.itbh.bev.api.data.BevQueryResult;
import at.itbh.bev.api.data.GeocodingResult;
import at.itbh.bev.api.data.QueryResult;
import at.itbh.bev.api.exceptions.InvalidApiUsageException;
import at.itbh.bev.index.TextAnalyzer;
import at.itbh.bev.jpa.AdresseDenormalized;

public class FinderImpl {

	Pattern housenumberPattern = Pattern.compile("^[^\\d]*(\\d+)", Pattern.CASE_INSENSITIVE);

	public FullTextQuery constructQuery(EntityManager em, String postalCode, String place, String addressLine,
			String houseId) throws InvalidApiUsageException {
		FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);

		if ((Objects.toString(postalCode, "") + Objects.toString(place, "") + Objects.toString(addressLine, "")
				+ Objects.toString(houseId, "")).length() == 0) {
			throw new InvalidApiUsageException(
					"At least one parameter must be provided. Coordinates don't count as parameters.");
		}

		if (addressLine != null && addressLine.length() < 2 && addressLine.length() > 0) {
			throw new InvalidApiUsageException("The parameter addressLine must consist of at least 2 characters.");
		}

		QueryBuilder b = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(AdresseDenormalized.class).get();
		List<Query> queries = new ArrayList<>();

		if (postalCode != null && postalCode.length() > 0) {
			queries.add(b.keyword().onField("postalCode").boostedTo(20).matching(postalCode).createQuery());
		}

		if (addressLine != null && addressLine.length() > 0) {
			queries.add(b.keyword().onField("addressLine").matching(addressLine + addressLine + addressLine).createQuery());
			// triple addressLine since in the data source it is also tripled if
			// there is no building or address name
			queries.add(b.keyword().onField("addressLineExact").boostedTo(10)
					.matching(addressLine + addressLine + addressLine).createQuery());
		}

		if (houseId != null && houseId.length() > 0) {
			// if search string contains a number, take the first number in the
			// search string and match with the house number

			Matcher matcher = housenumberPattern.matcher(houseId);
			if (matcher.find()) {
				queries.add(b.keyword().onField("hausnrzahl").boostedTo(50).matching(matcher.group(1)).createQuery());
			}
			
			if (houseId.matches(".*\\D.*")) {
				queries.add(b.keyword().onField("houseIdExact").matching(houseId).createQuery());			
			}
			
			queries.add(b.keyword().onField("houseId").boostedTo(20).matching(houseId).createQuery());
			
			TextAnalyzer analyzer = new TextAnalyzer();
			TokenStream stream;
			try {
				stream = analyzer.tokenStream(null, new StringReader(houseId));
				// CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
				stream.reset();
				if (stream.incrementToken()) {
					// if analyzer does not remove everything check hofname and hausnrgebaeudebez
					queries.add(b.keyword().onField("hofname").matching(houseId).createQuery());
					queries.add(b.keyword().onField("hausnrgebaeudebez").matching(houseId).createQuery());
					// System.out.println(cattr.toString());
				}
				stream.end();
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			analyzer.close();
		}

		if (place != null && place.length() > 0) {
			queries.add(b.keyword().onField("place").matching(place).createQuery());

			queries.add(b.keyword().onField("municipalityExact").boostedTo(20).matching(place).createQuery());
			queries.add(b.keyword().onField("placeExact").boostedTo(5).matching(place).createQuery());
		}

		@SuppressWarnings("rawtypes")
		BooleanJunction bq = b.bool();
		for (Query item : queries) {
			bq = bq.should(item);
		}

		FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(bq.createQuery(), AdresseDenormalized.class);
		return fullTextQuery;
	}

	public <T extends QueryResult> GeocodingResult<T> queryIndex(Class<T> resultType, FullTextQuery fullTextQuery,
			int maxResults, boolean prune, String postalCode, String place, String addressLine, String houseId,
			Double latitude, Double longitude, Float radius) throws InstantiationException, IllegalAccessException {

		fullTextQuery.setMaxResults(maxResults + 1);

		if (latitude != null && longitude != null) {
			fullTextQuery.setSpatialParameters(latitude, longitude, "location");
		}

		fullTextQuery.setProjection(FullTextQuery.THIS, FullTextQuery.SCORE, FullTextQuery.SPATIAL_DISTANCE);

		GeocodingResult<T> result = new GeocodingResult<T>();
		result.getRequest().setPostalCode(postalCode);
		result.getRequest().setPlace(place);
		result.getRequest().setAddressLine(addressLine);
		result.getRequest().setHouseId(houseId);
		result.getRequest().setLatitude(latitude);
		result.getRequest().setLongitude(longitude);
		result.getRequest().setRadius(radius);

		float previousScore = 0f;
		float previousDiff = 0f;
		T previousResult = null;
		for (Object r : fullTextQuery.getResultList()) {
			T bqr = resultType.newInstance();
			if (bqr instanceof BevQueryResult) {
				((BevQueryResult) bqr).setAddress((AdresseDenormalized) ((Object[]) r)[0]);
			}
			if (bqr instanceof AustrianCommonQueryResult) {
				((AustrianCommonQueryResult) bqr).setAddress((AdresseDenormalized) ((Object[]) r)[0]);
			}

			float score = (float) ((Object[]) r)[1];
			float diff = previousScore - score;
			// prune results if requested
			if (previousScore != 0f) {
				if (prune && diff * 1.9 < previousDiff)
					break;
			}
			previousScore = score;
			previousDiff = diff;
			bqr.setScore(score);

			Double distance = (Double) ((Object[]) r)[2];
			if (distance != null) {
				bqr.setDistance(distance);
			}

			result.addResult(previousResult);
			previousResult = bqr;
		}
		return result;
	}

}
