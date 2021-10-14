package de.accso.library.borrow;

import de.accso.library.borrow.impl.AvailableMediaState;
import de.accso.library.borrow.impl.BorrowedMediaState;
import de.accso.library.borrow.impl.ReservedMediaState;
import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Media;

/**
 * Common interface for states of {@link Borrowable} media, according to the GoF State Pattern.
 */
public interface BorrowableMediaState {

	/**
	 * Enum type to indicate, if a borrowable media is available to actually be
	 * borrowed. The order of its Enum instances specifies, which of multiple
	 * {@link Borrowable}s to prefer for borrowing.
	 */
	enum AvailabilityForBorrow {
		/** Indicates a media instance ist available for borrowing. */
		READY,

		/**
		 * Indicates a media instance ist currently borrowed, but will shortly be
		 * available for borrowing.
		 */
		SHORTLY,

		/**
		 * Indicates a media instance ist currently borrowed and already reserved, thus
		 * not available for borrowing.
		 */
		NONE
	}

	/** Singleton instance representing an available Media. */
	static BorrowableMediaState AVAILABLE = AvailableMediaState.getInstance();

	/** Singleton instance representing a borrowed Media. */
	static BorrowableMediaState BORROWED = BorrowedMediaState.getInstance();

	/** Singleton instance representing a reserved Media. */
	static BorrowableMediaState RESERVED = ReservedMediaState.getInstance();

	/**
	 * Indicates if a borrowable media of this state is available to actually be
	 * borrowed.
	 * <p/>
	 * Can be used for ordering to determine, which of multiple {@link Borrowable}s
	 * to prefer for borrowing.
	 *
	 * @return one of the enum values READY, SHORTLY or NONE.
	 */
	AvailabilityForBorrow getAvailability();

	/**
	 * Handles the transition of the given {@link Borrowable} from its current state
	 * to its new state upon borrowing its media.
	 *
	 * @param borrowable the media instance to borrow.
	 * @return The new state of the given {@link Borrowable}.
	 * @see BorrowService#borrow(java.util.List, de.accso.library.datamanagement.model.Customer)
	 */
	BorrowableMediaState borrowMedia(Borrowable<Media> borrowable);

	/**
	 * Handles the transition of the given {@link Borrowable} from its current state
	 * to its new state upon returning its media.
	 *
	 * @param borrowable the media instance to return.
	 * @return The new state of the given {@link Borrowable}.
	 * @see BorrowService#returnLoans(java.util.List, de.accso.library.datamanagement.model.Customer)
	 */
	BorrowableMediaState returnMedia(Borrowable<Media> borrowable);

}
