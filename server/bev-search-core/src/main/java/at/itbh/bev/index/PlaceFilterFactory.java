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

import at.itbh.bev.jpa.AdresseDenormalized;

/**
 * Restrict an index query to result with a municipality or place similar to the
 * one set with {@link #setPlace(String)}
 */
public class PlaceFilterFactory {

	private EntityManager entityManager;

	private String place;

	/**
	 * injected parameter
	 */
	public void setPlace(String place) {
		this.place = place;
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

		Query query = b.bool().should(b.keyword().onField("place").matching(place).createQuery())
				.should(b.keyword().onField("municipality").matching(place).createQuery()).createQuery();

		return new CachingWrapperFilter(new QueryWrapperFilter(query));
	}
}