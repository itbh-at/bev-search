package at.itbh.bev.api.exceptions;

/**
 * This exception is thrown if a computation or data query provides more results
 * than expected. E.g. when querying the database expecting a single record but
 * having more than one result.
 */
public abstract class TooManyResultsException extends Exception {

	private static final long serialVersionUID = 1L;

	public TooManyResultsException() {
		super("More results than expected have been returned.");
	}
}
