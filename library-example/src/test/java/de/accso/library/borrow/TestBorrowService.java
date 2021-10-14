package de.accso.library.borrow;

import de.accso.library.datamanagement.model.*;
import de.accso.library.testutil.AbstractTestWithDataFromExcelSheet;
import de.accso.library.util.UUIDGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TestBorrowService extends AbstractTestWithDataFromExcelSheet {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestBorrowService.class);

	@Autowired
	private BorrowService borrowAndReturnBooks;

	private static String titleGespenst = "Das Gespenst aus der b58";
	private static String titleCC = "Clean Code";
	private static String titleAgileSD = "Agile Software Development";
	private static String titleGreatMovie = "great Movie";

	@Test
	public void testBorrowBookOk() {
		// we use 2 books and one instance for each.
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);
		Customer c = getCustomerAndPayAllFees("Paul");
		List<Book> booksToBorrow = new ArrayList<>();
		booksToBorrow.add(bookDao.findByTitle(titleGespenst));
		booksToBorrow.add(bookDao.findByTitle(titleCC));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrow, c);
		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l = loans.get(0);

		assertThat(l.getMediaInstancesInLoan(), hasSize(2));

		assertThat(loanDao.findAll(), hasSize(1));

		LOGGER.debug("loan: \n" + l);
	}

	@Test
	public void testBorrowBookBookBorrowed() {
		// we use 2 books(one available, one borrowd) and one instance for each.
		createInstanceFor(titleGespenst);
		createBorrowedInstanceFor(titleCC);
		Customer c = getCustomerAndPayAllFees("Paul");
		List<Book> booksToBorrow = new ArrayList<>();
		booksToBorrow.add(bookDao.findByTitle(titleGespenst));
		booksToBorrow.add(bookDao.findByTitle(titleCC));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrow, c);
		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l = loans.get(0);

		// expected: one book in Loan, one borrowd, one reserved
		assertThat(l.getMediaInstancesInLoan(), hasSize(2));

		assertTrue(isBorrowed(bookDao.findByTitle(titleGespenst)));
		assertTrue(isReserved(bookDao.findByTitle(titleCC)));

		LOGGER.debug("loan: \n" + l);
	}

	@Test
	public void testBorrowBookBookReserved() {
		// we use 2 books (one borrowd, one reserved) and one instance for each.
		createBorrowedInstanceFor(titleGespenst);
		createReservedInstanceFor(titleCC);
		Customer c = getCustomerAndPayAllFees("Paul");
		List<Book> booksToBorrow = new ArrayList<>();
		booksToBorrow.add(bookDao.findByTitle(titleGespenst));
		booksToBorrow.add(bookDao.findByTitle(titleCC));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrow, c);
		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l = loans.get(0);

		// expected: only borrowed book in Loan, now reserved
		assertThat(l.getMediaInstancesInLoan(), hasSize(1));

		Borrowable<Media> b = l.getMediaInstancesInLoan().get(0);
		assertEquals(titleGespenst, b.getMedia().getTitle());
		assertTrue(isReserved(bookDao.findByTitle(titleGespenst)));

		LOGGER.debug("loan: \n" + l);
	}

	@Test
	public void testBorrowBookBookNotAvailable() {
		// three books, one instance for each this time.
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);
		createInstanceFor(titleAgileSD);

		// first customer
		Customer c1 = getCustomerAndPayAllFees("Paul");
		List<Book> booksToBorrowC1 = new ArrayList<>();
		booksToBorrowC1.add(bookDao.findByTitle(titleGespenst));
		booksToBorrowC1.add(bookDao.findByTitle(titleCC));

		borrowAndReturnBooks.borrow(booksToBorrowC1, c1);

		// second customer
		Customer c2 = getCustomerAndPayAllFees("Martin");
		List<Book> booksToBorrowC2 = new ArrayList<>();

		// Martin wants to borrow "CleanCode", too. But there is only one instance.
		booksToBorrowC2.add(bookDao.findByTitle(titleCC));
		booksToBorrowC2.add(bookDao.findByTitle(titleAgileSD));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrowC2, c2);

		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l2 = loans.get(0);

		// expected: both books in Loan, one borrowd, one reserved
		assertThat(l2.getMediaInstancesInLoan(), hasSize(2));

		assertThat(l2.getMediaInstancesInLoan().stream().map(b -> b.getMedia().getTitle()).collect(toList()),
				containsInAnyOrder(titleAgileSD, titleCC));
		assertTrue(isBorrowed(bookDao.findByTitle(titleAgileSD)));
		assertTrue(isReserved(bookDao.findByTitle(titleCC)));

		LOGGER.debug("loan: \n" + l2);
	}

	@Test
	public void testBorrowBookBookSeveralInstances() {
		// three books, one instance for each, two for Clean Code
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);
		createInstanceFor(titleCC);
		createInstanceFor(titleAgileSD);

		// first customer
		Customer c1 = getCustomerAndPayAllFees("Paul");
		List<Book> booksToBorrowC1 = new ArrayList<>();
		booksToBorrowC1.add(bookDao.findByTitle(titleGespenst));
		booksToBorrowC1.add(bookDao.findByTitle(titleCC));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrowC1, c1);
		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l1 = loans.get(0);

		assertThat(l1.getMediaInstancesInLoan(), hasSize(2));
		assertTrue("One Clean Code should still be available", isAvailable(bookDao.findByTitle(titleCC)));

		// second customer
		Customer c2 = getCustomerAndPayAllFees("Martin");
		List<Book> booksToBorrowC2 = new ArrayList<>();

		// Martin wants to borrow "CleanCode", too. But there is only one instance.
		booksToBorrowC2.add(bookDao.findByTitle(titleCC));
		booksToBorrowC2.add(bookDao.findByTitle(titleAgileSD));

		List<Loan> loans2 = borrowAndReturnBooks.borrow(booksToBorrowC2, c2);
		assertThat("Expected at least one loan", loans2, not(empty()));
		Loan l2 = loans2.get(0);

		assertNotNull(l2);
		assertThat(l2.getMediaInstancesInLoan(), hasSize(2));

		assertFalse(isAvailable(bookDao.findByTitle(titleCC)));
		assertFalse(isAvailable(bookDao.findByTitle(titleGespenst)));
		assertFalse(isAvailable(bookDao.findByTitle(titleAgileSD)));

		LOGGER.debug("loan: \n" + l2);
	}

	@Test
	public void testBorrowBookDoubleBookingNotAvailable() {
		// three books, one of them with two instances (available and reserved)
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);
		createReservedInstanceFor(titleCC);	// reserved book canot be borrowed
		createInstanceFor(titleAgileSD);

		// first customer books first instance
		Customer c1 = getCustomerAndPayAllFees("Paul");
		List<Book> booksToBorrowC1 = new ArrayList<>();
		booksToBorrowC1.add(bookDao.findByTitle(titleGespenst));
		booksToBorrowC1.add(bookDao.findByTitle(titleCC));

		borrowAndReturnBooks.borrow(booksToBorrowC1, c1);

		// second customer
		Customer c2 = getCustomerAndPayAllFees("Martin");
		List<Book> booksToBorrowC2 = new ArrayList<>();

		// Martin wants to borrow "CleanCode", too. Accidentially he books thwo instances, but there is only one instance left.
		booksToBorrowC2.add(bookDao.findByTitle(titleCC));
		booksToBorrowC2.add(bookDao.findByTitle(titleCC));

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrowC2, c2);

		assertThat("Expected one loan", loans, hasSize(1));
		Loan l2 = loans.get(0);

		assertThat(l2.getMediaInstancesInLoan(), hasSize(1));

		assertEquals(titleCC, l2.getMediaInstancesInLoan().get(0).getMedia().getTitle());
		LOGGER.debug("loan: \n" + l2);
	}

	@Test
	public void testBorrowBookNokCustomerNotRegistered() {
		Customer c1 = new Customer("Ich habe mich noch nicht registiert", "nirgendwo");
		List<Book> booksToBorrowC1 = new ArrayList<>();

		Book bookGespenst = bookDao.findByTitle(titleGespenst);
		Book bookCleanCode = bookDao.findByTitle(titleCC);

		// create one instance for each
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);

		booksToBorrowC1.add(bookGespenst);
		booksToBorrowC1.add(bookCleanCode);

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrowC1, c1);

		assertThat("No loans expected - customer ist not authorized because not registered ", loans, is(empty()));

		assertTrue("Expected book to be still available: '" + bookDao.findByTitle(titleGespenst) + "'",
				isAvailable(bookGespenst));
	}

	@Test
	public void testReturnBooks() {
		// create some instances
		createInstanceFor(titleCC);
		createInstanceFor(titleAgileSD);
		createInstanceFor(titleGespenst);

		// borrow...
		Customer c = getCustomerAndPayAllFees("Martin");

		Book bookCleanCode = bookDao.findByTitle(titleCC);
		Book bookAgilSD = bookDao.findByTitle(titleAgileSD);

		List<Book> booksToBorrow = Arrays.asList(bookCleanCode, bookAgilSD);

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrow, c);
		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l = loans.get(0);

		List<Borrowable<Media>> bisToReturn = new ArrayList<>();
		List<Borrowable<Media>> bisInLoan = l.getMediaInstancesInLoan();
		// Only return "Clean Code" - make sure we return the instance we borrowed.
		for (Borrowable<Media> bi : bisInLoan) {
			if (bi.getMedia().getTitle().equals(titleCC)) {
				bisToReturn.add(bi);
			}
		}

		borrowAndReturnBooks.returnLoans(bisToReturn, c);

		List<Loan> loansForC = loanDao.findByCustomer(c);

		// Nur eines von zwei Büchern wurde zurückgegeben.
		assertThat(loansForC, hasSize(1));

		Loan loanAfterReturn = loansForC.get(0);
		assertThat(loanAfterReturn.getMediaInstancesInLoan(), hasSize(1));

		// Das zurückgegebene Buch ist wieder verfügbar
		assertTrue("Book should be available again", isAvailable(bookCleanCode));
	}

	@Test
	public void testReturnBooksBookReserved() {
		// create some instances
		createBorrowedInstanceFor(titleCC);
		createInstanceFor(titleAgileSD);
		createInstanceFor(titleGespenst);

		// borrow...
		Customer c = getCustomerAndPayAllFees("Martin");

		Book bookCleanCode = bookDao.findByTitle(titleCC);
		Book bookAgilSD = bookDao.findByTitle(titleAgileSD);

		// Martin borrows "CleanCode", too. But the instance is already borrowed.
		List<Book> booksToBorrow = Arrays.asList(bookCleanCode, bookAgilSD);

		List<Loan> loans = borrowAndReturnBooks.borrow(booksToBorrow, c);
		assertThat("Expected at least one loan", loans, not(empty()));
		Loan l = loans.get(0);

		// Only return "Clean Code" - make sure we return the instance we borrowed.
		List<Borrowable<Media>> bisToReturn = l.getMediaInstancesInLoan().stream()
				.filter(bi -> bi.getMedia().getTitle().equals(titleCC)).collect(toList());
		borrowAndReturnBooks.returnLoans(bisToReturn, c);

		List<Loan> loansForC = loanDao.findByCustomer(c);

		// Nur eines von zwei Büchern wurde zurückgegeben.
		assertThat(loansForC, hasSize(1));

		Loan loanAfterReturn = loansForC.get(0);
		assertThat(loanAfterReturn.getMediaInstancesInLoan(), hasSize(1));

		// Das zurückgegebene Buch ist sofort wieder verliehen
		assertTrue("Book should be available again", isBorrowed(bookCleanCode));
	}

	@Test
	public void testBorrowDifferentMediaOk() {
		Movie m1 = new Movie(titleGreatMovie, "Me", StorageType.BLUERAY);
		movieDao.save(m1);
		createInstanceFor(titleGreatMovie);

		// books
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);

		Customer c = getCustomerAndPayAllFees("Paul");
		List<Media> stuffToBorrow = new ArrayList<>();
		stuffToBorrow.add(bookDao.findByTitle(titleGespenst));
		stuffToBorrow.add(bookDao.findByTitle(titleCC));
		stuffToBorrow.add(movieDao.findByTitle(titleGreatMovie));

		List<Loan> loans = borrowAndReturnBooks.borrow(stuffToBorrow, c);
		assertThat("Expected at least two loans", loans, hasSize(greaterThan(1)));

		for (Loan loan : loans) {
			// We only look at them on the command line...
			LOGGER.debug("Found loan: \n" + loan);
		}
	}

	@Test
	public void testSomeFeesPayedSomeNot() {
		Movie m1 = new Movie(titleGreatMovie, "Me", StorageType.BLUERAY);
		movieDao.save(m1);
		createInstanceFor(titleGreatMovie);

		Movie m2 = new Movie(titleGreatMovie + " XX ", "Me again", StorageType.DVD);
		movieDao.save(m2);
		createInstanceFor(m2.getTitle());

		// books
		createInstanceFor(titleGespenst);
		createInstanceFor(titleCC);

		Customer c = customerDao.findByName("Paul");
		CustomerAccounting acc = new CustomerAccounting();

		// Pay some fees, some not
		// Books are still for free!
		// acc.payFeeUntil(MediaType.BOOK, LocalDateTime.now().plusDays(10));
		acc.payFeeUntil(MediaType.MOVIE_ON_BLURAY, LocalDateTime.now().minusDays(10));
		c.setAccounting(acc);
		c = customerDao.save(c);

		List<Media> stuffToBorrow = new ArrayList<>();
		stuffToBorrow.add(bookDao.findByTitle(titleGespenst));
		stuffToBorrow.add(bookDao.findByTitle(titleCC));
		stuffToBorrow.add(movieDao.findByTitle(titleGreatMovie));
		stuffToBorrow.add(movieDao.findByTitle(m2.getTitle()));

		List<Loan> loans = borrowAndReturnBooks.borrow(stuffToBorrow, c);

		// Expecting: Loan for Books but none for movies.
		assertThat("Expected one loan", loans, hasSize(1));

		for (Loan loan : loans) {
			LOGGER.debug("Found loan: \n" + loan);
			assertEquals(MediaType.BOOK, MediaType.getMediaTypeFor(loan.getMediaInstancesInLoan().get(0).getMedia()));
		}
	}

	/**
	 * Creates a {@link Borrowable} instance in the database for the media with the
	 * given title and media state {@link BorrowableMediaState#AVAILABLE}.
	 */
	private Borrowable<Media> createInstanceFor(String title) {
		Media media = mediaDao.findByTitle(title);
		if (media != null) {
			Borrowable<Media> bi = new Borrowable<>(UUIDGenerator.getUUID().toString(), true, media);
			return borrowableDao.save(bi);
		}
		return null;
	}

	/**
	 * Creates a {@link Borrowable} instance in the database for the media with the
	 * given title and media state {@link BorrowableMediaState#AVAILABLE}.
	 */
	private Borrowable<Media> createBorrowedInstanceFor(String title) {
		Media media = mediaDao.findByTitle(title);
		if (media != null) {
			Borrowable<Media> bi = new Borrowable<>(UUIDGenerator.getUUID().toString(), false, media);
			bi.setReserved(false);
			return borrowableDao.save(bi);
		}
		return null;
	}

	/**
	 * Creates a {@link Borrowable} instance in the database for the media with the
	 * given title and media state {@link BorrowableMediaState#RESERVED}.
	 */
	private Borrowable<Media> createReservedInstanceFor(String title) {
		Media media = mediaDao.findByTitle(title);
		if (media != null) {
			Borrowable<Media> bi = new Borrowable<>(UUIDGenerator.getUUID().toString(), false, media);
			bi.setReserved(true);
			return borrowableDao.save(bi);
		}
		return null;
	}

	private boolean isAvailable(Book book) {
		Set<Borrowable<Media>> borrowables = borrowableDao.findByMedia(book);
		return borrowables.stream().anyMatch(b -> b.isAvailable() && !b.isReserved());
	}

	private boolean isBorrowed(Book book) {
		Set<Borrowable<Media>> borrowables = borrowableDao.findByMedia(book);
		return borrowables.stream().anyMatch(b -> !b.isAvailable() && !b.isReserved());
	}

	private boolean isReserved(Book book) {
		Set<Borrowable<Media>> borrowables = borrowableDao.findByMedia(book);
		return borrowables.stream().anyMatch(b -> !b.isAvailable() && b.isReserved());
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
