package de.accso.library.borrow.impl;

import de.accso.library.borrow.BorrowableMediaState;
import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete {@link BorrowableMediaState} for an available media.
 */
public class AvailableMediaState implements BorrowableMediaState {

	private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMediaState.class);

	private static final AvailableMediaState INSTANCE = new AvailableMediaState();

	/**
	 * @return the Singleton instance representing an available Media.
	 */
	public static AvailableMediaState getInstance() {
		return INSTANCE;
	}

	private AvailableMediaState() {
		// private constructor of singleton class
	}

	/**
	 * @return the enum value {@link AvailabilityForBorrow#READY}.
	 */
	@Override
	public AvailabilityForBorrow getAvailability() {
		return AvailabilityForBorrow.READY;
	}

	@Override
	public BorrowableMediaState borrowMedia(Borrowable<Media> borrowable) {
		LOGGER.debug("Media is available: '{}', instance in loan: {}", borrowable.getMedia().getTitle(), borrowable);
		borrowable.setAvailable(false);
		return BORROWED;
	}

	@Override
	public BorrowableMediaState returnMedia(Borrowable<Media> borrowable) {
		// no change of state
		LOGGER.warn("Available media cannot be returned: '{}'", borrowable.getMedia().getTitle());
		return this;
	}

	@Override
	public String toString() {
		return "AVAILABLE";
	}

}
