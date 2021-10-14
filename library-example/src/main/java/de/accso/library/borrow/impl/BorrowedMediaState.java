package de.accso.library.borrow.impl;

import de.accso.library.borrow.BorrowableMediaState;
import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete {@link BorrowableMediaState} for a borrowed media.
 */
public class BorrowedMediaState implements BorrowableMediaState {

	private static final Logger LOGGER = LoggerFactory.getLogger(BorrowedMediaState.class);

	private static final BorrowedMediaState INSTANCE = new BorrowedMediaState();

	/**
	 * @return the Singleton instance representing a borrowed Media.
	 */
	public static BorrowedMediaState getInstance() {
		return INSTANCE;
	}

	private BorrowedMediaState() {
		// private constructor of singleton class
	}

	/**
	 * @return the enum value {@link AvailabilityForBorrow#SHORTLY}.
	 */
	@Override
	public AvailabilityForBorrow getAvailability() {
		return AvailabilityForBorrow.SHORTLY; // borrow als fall-back (reserve if none available)
	}

	@Override
	public BorrowableMediaState borrowMedia(Borrowable<Media> borrowable) {
		LOGGER.debug("Reserving borrowed media: '{}'", borrowable.getMedia().getTitle());
		borrowable.setReserved(true);
		return RESERVED;
	}

	@Override
	public BorrowableMediaState returnMedia(Borrowable<Media> borrowable) {
		LOGGER.debug("Returning borrowed media: '{}'", borrowable.getMedia().getTitle());
		borrowable.setAvailable(true);
		return AVAILABLE;
	}

	@Override
	public String toString() {
		return "BORROWED";
	}

}
