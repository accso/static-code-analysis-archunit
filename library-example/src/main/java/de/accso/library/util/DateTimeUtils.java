package de.accso.library.util;

import java.time.LocalDateTime;

/**
 * Utility functions for the {@link LocalDateTime} class.
 */
public class DateTimeUtils {

	private static DateTimeUtils instance;

	public static DateTimeUtils getInstance() {
		// As this application is single-threaded, no need for (double-checked) locking
		if (instance == null) {
			instance = new DateTimeUtils();
		}
		return instance;
	}

	/**
	 * Returns <code>true</code> if and only if <code>toCheck</code> is
	 * <ol>
	 * <li>greater or equal than <code>start</code> and</li>
	 * <li>less or equal than <code>end</code>.</li>
	 * </ol>
	 */
	public boolean isDateTimeBetween(LocalDateTime toCheck, LocalDateTime start, LocalDateTime end) {
		return (toCheck.isEqual(start) || toCheck.isAfter(start)) && (toCheck.isBefore(end) || toCheck.isEqual(end));
	}

}
