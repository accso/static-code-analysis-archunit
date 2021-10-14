package de.accso.library.borrow;

public class BorrowBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1532488022632792654L;

	public BorrowBusinessException() {
	}

	public BorrowBusinessException(String message) {
		super(message);
	}

	public BorrowBusinessException(Throwable cause) {
		super(cause);
	}

	public BorrowBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BorrowBusinessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
