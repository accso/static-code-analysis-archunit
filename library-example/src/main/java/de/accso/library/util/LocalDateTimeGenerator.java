package de.accso.library.util;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Generator for {@link LocalDateTime} instances.
 */
public class LocalDateTimeGenerator {

	private static LocalDateTimeGenerator instance;
	private static Random prnp;

	public static LocalDateTimeGenerator getInstance() {
		// As this application is single-threaded, no need for (double-checked) locking
		if (instance == null) {
			prnp = new Random(System.currentTimeMillis());
			instance = new LocalDateTimeGenerator();
		}
		return instance;
	}

	private LocalDateTimeGenerator() {
		// hidden default constructor
	}

	/**
	 * Generates a random {@link LocalDateTime} for the given <code>year</code>.
	 */
	public LocalDateTime randomDateTimeForYear(int year) {
		int minute = prnp.nextInt(60);
		int hour = prnp.nextInt(24);
		int day = prnp.nextInt(28) + 1;
		int month = prnp.nextInt(12) + 1;
		return LocalDateTime.of(year, month, day, hour, minute, 0);
	}

}
