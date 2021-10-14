package de.accso.library.customers;

public class CustomerBusinessException extends RuntimeException {

	private static final long serialVersionUID = 2332697046025604447L;

	public CustomerBusinessException() {
	}

	public CustomerBusinessException(String message) {
		super(message);
	}

	public CustomerBusinessException(Throwable cause) {
		super(cause);
	}

	public CustomerBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerBusinessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
