package at.itbh.bev.rest.client;

public class BevRestException extends Exception {
	private static final long serialVersionUID = 1L;
	private int errorCode;
	
	public BevRestException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return this.errorCode;
	}
}
