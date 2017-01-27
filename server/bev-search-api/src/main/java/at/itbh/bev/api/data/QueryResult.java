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
