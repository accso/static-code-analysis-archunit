package de.accso.library.borrow.impl;

import de.accso.library.datamanagement.model.Media;
import de.accso.library.datamanagement.model.MediaType;

import java.time.Period;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains properties for borrowing media from the library
 */
public enum BorrowProperties {
	BOOK(Period.ofMonths(1)), MUSIC(Period.ofWeeks(2)), MOVIE(Period.ofWeeks(2));

	// "leihfrist"
	private Period loanPeriod;

	public Period getLoanPeriod() {
		return loanPeriod;
	}

	private BorrowProperties(Period loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	private static final Map<MediaType, BorrowProperties> propertyMap = new HashMap<>();

	static {
		propertyMap.put(MediaType.BOOK, BorrowProperties.BOOK);
		propertyMap.put(MediaType.MOVIE_ON_BLURAY, BorrowProperties.MOVIE);
		propertyMap.put(MediaType.MOVIE_ON_DVD, BorrowProperties.MOVIE);
		propertyMap.put(MediaType.MUSIC, BorrowProperties.MUSIC);
	}

	public static BorrowProperties getBorrowPropertiesFor(Media media) {
		return propertyMap.get(MediaType.getMediaTypeFor(media));
	}

	public static BorrowProperties getBorrowPropertiesFor(MediaType mt) {
		return propertyMap.get(mt);
	}
}
