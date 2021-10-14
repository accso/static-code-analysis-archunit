package de.accso.library.borrow.impl;

import de.accso.library.borrow.BorrowBusinessException;
import de.accso.library.borrow.BorrowService;
import de.accso.library.borrow.BorrowableMediaState;
import de.accso.library.common.Authorization;
import de.accso.library.datamanagement.manager.BorrowableDao;
import de.accso.library.datamanagement.manager.LoanDao;
import de.accso.library.datamanagement.manager.MediaDao;
import de.accso.library.datamanagement.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Component
public class BorrowServiceImpl implements BorrowService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BorrowServiceImpl.class);

	@Autowired
	private MediaDao mediaDao;

	@Autowired
	private BorrowableDao borrowableDao;

	@Autowired
	private LoanDao loanDao;

	@Autowired
	private Authorization authorization;

	@Override
	public <T extends Media> List<Loan> borrow(List<T> mediaToBorrow, Customer customer)
			throws BorrowBusinessException {

		// We may have different media with different loan periods
		// We therefore need a different loan for each loan period.

		// In Addition, we have different security checks for different media types.
		// So we first sort by media type and then handle each media type.

		Map<MediaType, List<T>> mediaToBorrowByType = sortMediaByType(mediaToBorrow);

		List<Loan> loans = new ArrayList<>();
		for (MediaType mt : mediaToBorrowByType.keySet()) {
			if (authorization.isCustomerAuthorizedToBorrow(customer, mt)) {
				Loan loan = borrowForMediaType(mt, mediaToBorrowByType.get(mt), customer);
				if (loan.getMediaInstancesInLoan().size() > 0) {
					loan = loanDao.save(loan);
					loans.add(loan);
				}
			}
		}
		return loans;
	}

	/**
	 * Returns one loan for all borrowable media of one type.
	 *
	 * @param mt
	 * @param mediaToBorrowOneMediaType
	 * @param customer
	 * @return
	 */
	private Loan borrowForMediaType(MediaType mt, Iterable<? extends Media> mediaToBorrowOneMediaType, Customer customer) {

		// for one media type all media have the same due date
		LocalDateTime dueDate = calculateDueDate(mt);
		List<Borrowable<Media>> borrowables = new ArrayList<>();

		for (Media media : mediaToBorrowOneMediaType) {

			Media mediaInLibrary = mediaDao.findByTitle(media.getTitle());
			// check if media exists in library...
			if (mediaInLibrary == null || !mediaInLibrary.equals(media)) {
				// nothing can be done about this :-) Proceed with next one
				LOGGER.info("media '{}' is not part of this library", media);
				continue;
			}

			Collection<Borrowable<Media>> instancesOfMedia = mediaInLibrary.getAllInstances();
			// check if a media is available - take the first we find!
			Optional<Borrowable<Media>> availableInstance = instancesOfMedia.stream()
					.sorted(comparing(b -> b.getMediaState().getAvailability())).findFirst();
			if (availableInstance.isPresent()) {
				Borrowable<Media> borrowable = availableInstance.get();
				BorrowableMediaState oldState = borrowable.getMediaState();
				BorrowableMediaState newState = oldState.borrowMedia(borrowable);
				if (!newState.equals(oldState)) {
					borrowables.add(borrowable);
					// ... and make this instance unavailable on DB
					borrowableDao.save(borrowable);
				}
			} else {
				LOGGER.info("media '{}' has no borrowable instances in this library", media);
			}
		}

		Loan loan = new Loan(LocalDateTime.now(), dueDate, customer, borrowables);
		return loan;
	}

	/**
	 * Returns the media requested to borrow, sorted my media type.
	 *
	 * @param mediaToBorrow
	 * @return
	 */
	private <T extends Media> Map<MediaType, List<T>> sortMediaByType(List<T> mediaToBorrow) {
		return mediaToBorrow.stream().collect(groupingBy(MediaType::getMediaTypeFor));
	}

	/**
	 * Calculate due date relative to midnight
	 *
	 * @param media
	 * @return
	 */
	private LocalDateTime calculateDueDate(MediaType mt) {
		// Calculate due date for this loan from individual loan period of media
		Period loanPeriod = BorrowProperties.getBorrowPropertiesFor(mt).getLoanPeriod();
		LocalDateTime dueDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plus(loanPeriod);
		return dueDate;
	}

	@Override
	public void returnLoans(List<Borrowable<Media>> instancesToReturn, Customer customer) {
		if (instancesToReturn == null || customer == null || instancesToReturn.isEmpty())
			return;

		List<Loan> loansOfCustomer = loanDao.findByCustomer(customer);
		if (loansOfCustomer.isEmpty())
			return; // nothing to do

		for (Loan loan : loansOfCustomer) {
			boolean loanContainsReturns = false;
			for (Borrowable<Media> borrowable : instancesToReturn) {
				if (loan.contains(borrowable)) {
					loanContainsReturns = true;
					// remove the returned book instance from loan and make it available again
					BorrowableMediaState oldState = borrowable.getMediaState();
					BorrowableMediaState newState = oldState.returnMedia(borrowable);
					// and persist this.
					if (!newState.equals(oldState)) {
						// First find media on db.
						Media mediaOfInstance = mediaDao.findByTitle(borrowable.getMedia().getTitle());
						if (mediaOfInstance == null || !mediaOfInstance.equals(borrowable.getMedia())) {
							LOGGER.warn("The media '{}' you are trying to return does not belong to our library",
									borrowable.getMedia().getTitle());
							continue;
						}
						loan.remove(borrowable);
						borrowableDao.save(borrowable);
					}
				}
			}
			if (loanContainsReturns) {
				loanDao.save(loan);
			}
		}
	}

}
