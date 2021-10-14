package de.accso.library.borrow.impl;

import de.accso.library.borrow.BorrowableMediaState;
import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete {@link BorrowableMediaState} for a reserved media.
 */
public class ReservedMediaState implements BorrowableMediaState {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservedMediaState.class);

	private static final ReservedMediaState INSTANCE = new ReservedMediaState();

	/**
	 * @return the Singleton instance representing a reserved Media.
	 */
	public static ReservedMediaState getInstance() {
		return INSTANCE;
	}

	private ReservedMediaState() {
		// private constructor of singleton class
	}

	/**
	 * @return the enum value {@link AvailabilityForBorrow#NONE}.
	 */
	@Override
	public AvailabilityForBorrow getAvailability() {
		return AvailabilityForBorrow.NONE;
	}

	@Override
	public BorrowableMediaState borrowMedia(Borrowable<Media> borrowable) {
		// no change of state
		LOGGER.warn("Reserved media is not available for borrowing: '{}'", borrowable.getMedia().getTitle());
		return this;
	}

	@Override
	public BorrowableMediaState returnMedia(Borrowable<Media> borrowable) {
		LOGGER.debug("Cancelling reserved media: '{}'", borrowable.getMedia().getTitle());
		borrowable.setReserved(false);
		return BORROWED;
	}

	@Override
	public String toString() {
		return "RESERVED";
	}

}
