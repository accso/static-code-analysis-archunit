package de.accso.library.borrow;

import de.accso.library.borrow.impl.BorrowProperties;
import de.accso.library.datamanagement.model.*;
import de.accso.library.testutil.AbstractTestWithDataFromExcelSheet;
import de.accso.library.util.UUIDGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDueDates extends AbstractTestWithDataFromExcelSheet {

	@Autowired
	private BorrowService borrowAndReturnBooks;

	private String titleGespenst = "Das Gespenst aus der b58";
	private String titleCC = "Clean Code";
	// private String titleAgileSD = "Agile Software Development";

	private String titleGreatMovie = "great Movie";

	@Test
	public void testDueDatesBooks() {
		// we use 2 books and one instance for each.
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);
		Customer c = getCustomerAndPayAllFees("Paul");
		List<Media> booksToBorrow = new ArrayList<>();
		booksToBorrow.add(bookDao.findByTitle(titleGespenst));
		booksToBorrow.add(bookDao.findByTitle(titleCC));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrow, c);
		assertTrue("Expected at least one loan", loans.size() > 0);

		for (Loan l : loans) {
			assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
					.plusMonths(BorrowProperties.BOOK.getLoanPeriod().getMonths()), l.getDueDate());
		}
	}

	@Test
	public void testDueDatesMovies() {
		Movie m1 = new Movie(titleGreatMovie, "Me", StorageType.BLUERAY);
		movieDao.save(m1);
		createInstanceFor(titleGreatMovie);
		createUnavailableInstanceFor(titleGreatMovie);

		Customer c = getCustomerAndPayAllFees("Paul");
		Movie movie1 = movieDao.findByTitle(titleGreatMovie);
		Movie movie2 = movieDao.findByTitle(titleGreatMovie);
		List<Media> moviesToBorrow = Arrays.asList(movie1, movie2);

		List<Loan> loans = borrowAndReturnBooks.borrow(moviesToBorrow, c);
		assertTrue("Expected at least one loan", loans.size() > 0);

		for (Loan l : loans) {
			assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
					.plusDays(BorrowProperties.MOVIE.getLoanPeriod().getDays()), l.getDueDate());
		}
	}

	private Borrowable<Media> createInstanceFor(String title) {
		Borrowable<Media> bi = null;
		if (mediaDao.findByTitle(title) != null) {
			bi = new Borrowable<>(UUIDGenerator.getUUID().toString(), true, mediaDao.findByTitle(title));
			return borrowableDao.save(bi);
		}
		return bi;
	}

	private Borrowable<Media> createUnavailableInstanceFor(String title) {
		Borrowable<Media> bi = null;
		if (mediaDao.findByTitle(title) != null) {
			bi = new Borrowable<>(UUIDGenerator.getUUID().toString(), false, mediaDao.findByTitle(title));
			return borrowableDao.save(bi);
		}
		return bi;
	}

	private Customer getCustomerAndPayAllFees(String string) {
		Customer c = customerDao.findByName("Paul");
		CustomerAccounting acc = new CustomerAccounting();

		for (MediaType mt : MediaType.values()) {
			acc.payFeeUntil(mt, LocalDateTime.now().plusDays(10));
		}
		c.setAccounting(acc);
		return customerDao.save(c);
	}

}
