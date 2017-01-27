package at.itbh.bev.api.exceptions;

public class InvalidApiUsageException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidApiUsageException(String message) {
		super(message);
	}
}
