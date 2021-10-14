package de.accso.library.datamanagement;

import de.accso.library.ApplicationConfig;
import de.accso.library.datamanagement.manager.BookDao;
import de.accso.library.datamanagement.manager.BorrowableDao;
import de.accso.library.datamanagement.manager.CustomerDao;
import de.accso.library.datamanagement.manager.LoanDao;
import de.accso.library.datamanagement.model.*;
import de.accso.library.datamanagement.util.BooksDataLoader;
import de.accso.library.datamanagement.util.CustomerDataLoader;
import de.accso.library.testutil.H2ConsoleServerRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for DataManagemnt: creating, editing, finding, destroying data of all
 * types.
 */
@SpringBootTest(classes = { ApplicationConfig.class })
@RunWith(SpringRunner.class)
public class TestDataManagement {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDataManagement.class);

	@ClassRule
	public static H2ConsoleServerRule consoleServerRule = new H2ConsoleServerRule();

	private final String customerDataFile = "/testdata/datamanagement/Customer.xlsx";
	private final String bookDataFile = "/testdata/datamanagement/Book.xlsx";

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private BookDao bookDao;

	@Autowired
	private LoanDao loanDao;

	@Autowired
	private BorrowableDao borrowableDao;

	private CustomerDataLoader cdl;
	private BooksDataLoader bdl;

	@Before
	public void setUp() {
		cdl = new CustomerDataLoader(customerDao);
		bdl = new BooksDataLoader(bookDao);
		// ldl = new LoanDataLoader(bookDao, customerDao);
	}

	@After
	public void tearDown() {
		loanDao.deleteAll();
		customerDao.deleteAll();
		borrowableDao.deleteAll();
		bookDao.deleteAll();
	}

	@Test
	public void testCloneCustomer() {
		String name = "hirsch";
		String adress = "wald";

		Customer c = new Customer(name, adress);
		Customer cClone = c.clone();

		assertEquals(name, cClone.getName());
		assertEquals(adress, cClone.getAddress());
		assertEquals(c, cClone);
	}

	@Test
	public void testCloneBook() {
		String bookTitle = "This is a book";
		List<String> authors = Arrays.asList("Mister X", "Mister Y");
		Book b = new Book(bookTitle, authors);
		Book bClone = b.clone();

		assertEquals(bookTitle, bClone.getTitle());
		assertEquals(authors, bClone.getAuthors());
		assertEquals(authors.get(0), bClone.getAuthors().get(0));
		assertEquals(b, bClone);
	}

	@Test
	public void testCloneLoan() {
		Long id = 42L;
		Book b1 = new Book("Buchtitel 1", Arrays.asList("Berühmtheit", "Weniger berühmt"));
		Book b2 = new Book("Buchtitel 2", Arrays.asList("Ich"));

		Borrowable<Media> bor1 = new Borrowable<>("a", true, b1);
		Borrowable<Media> bor2 = new Borrowable<>("b", true, b2);

		List<Borrowable<Media>> bookinstances = Arrays.asList(bor1, bor2);

		Customer c = new Customer("hirsch", "wald");

		LocalDateTime loanDate = LocalDateTime.now();
		LocalDateTime dueDate = loanDate.plusMonths(1);
		Loan l = new Loan(id, loanDate, dueDate, c, bookinstances);
		Loan lClone = l.clone();

		assertEquals(id, lClone.getId());
		assertEquals(loanDate, lClone.getLoanDate());
		assertEquals(dueDate, lClone.getDueDate());
		assertEquals(c, lClone.getCustomer());
		assertEquals(bookinstances, lClone.getMediaInstancesInLoan());
		assertEquals(bookinstances.get(0), lClone.getMediaInstancesInLoan().get(0));
		assertEquals(l, lClone);
	}

	@Test
	public void testCRUDCustomer() throws IOException {

		String customersName = "hirsch";
		// create
		Customer c = new Customer(customersName, "wald");
		c = customerDao.save(c);

		assertNotNull(customerDao.findByName(customersName));

		// -- create - exceptions
		Customer sameAgain = new Customer(customersName, "feld");
		try {
			customerDao.save(sameAgain);
			fail("Expected DataIntegrityViolationException");
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("Caught expected exception -- " + e.getMessage());
		}

		// update
		String newAdress = "in den Bergen";
		Customer cToUpdate = c.clone();
		cToUpdate.setAddress(newAdress);
		cToUpdate = customerDao.save(cToUpdate);

		// read
		// insert some more data
		cdl.loadDataFromExcelSheet(customerDataFile);

		assertEquals(cToUpdate, customerDao.findByName(c.getName()));

		// destroy
		customerDao.delete(cToUpdate);
		assertNull(customerDao.findByName(cToUpdate.getName()));
		// Idempotence => No exception
		customerDao.delete(cToUpdate);
	}

	@Test
	public void testCRUDBook() throws IOException {

		String bookTitle = "This is a book";
		// create
		Book b = new Book(bookTitle, Arrays.asList("Berühmtheit", "Weniger berühmt"));
		b = bookDao.save(b);

		assertNotNull(bookDao.findByTitle(bookTitle));

		// -- create - exceptions
		Book sameAgain = new Book(bookTitle, Arrays.asList("Mister X", "Mister Y"));
		try {
			bookDao.save(sameAgain);
			fail("Expected DataIntegrityViolationException");
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("Caught expected exception -- " + e.getMessage());
		}

		// update
		String author1 = "Der eine";
		String author2 = "Der andere";
		List<String> newAuthors = Arrays.asList(author1, author2);
		Book bToUpdate = new Book(b.getId(), b.getTitle(), newAuthors);
		bToUpdate = bookDao.save(bToUpdate);
		assertEquals(bToUpdate, bookDao.findByTitle(bookTitle));

		// read
		// insert some more data
		bdl.loadDataFromExcelSheet(bookDataFile);

		Book anotherBookByAuthor2 = new Book("Noch ein Buch von Autor 2", Arrays.asList(author2));
		anotherBookByAuthor2 = bookDao.save(anotherBookByAuthor2);

		Collection<Book> booksByAuthor1 = bookDao.findByAuthorsContaining(author1);
		assertTrue(booksByAuthor1.size() == 1);
		assertEquals(bToUpdate, booksByAuthor1.iterator().next());

		Collection<Book> booksByAuthor2 = bookDao.findByAuthorsContaining(author2);
		assertTrue(booksByAuthor2.size() == 2);

		// destroy
		bookDao.delete(bToUpdate);
		assertNull(bookDao.findByTitle(bToUpdate.getTitle()));
		// Idempotence => No exception
		bookDao.delete(bToUpdate);
	}

	@Test
	public void testBookAvailability() {
		String bookTitle = "This is a book";
		// create
		Book b1 = new Book(bookTitle, Arrays.asList("Berühmtheit", "Weniger berühmt"));
		b1 = bookDao.save(b1);

		Borrowable<Media> bor1 = new Borrowable<>("a", true, b1);

		borrowableDao.save(bor1);
		assertTrue("Should  be available: " + b1, borrowableDao.findByMedia(b1).size() > 0);

	}

	@Test
	public void testCRUDLoan() {

		Book b1 = new Book("Buchtitel 1", Arrays.asList("Berühmtheit", "Weniger berühmt"));
		b1 = bookDao.save(b1);
		Book b2 = new Book("Buchtitel 2", Arrays.asList("Ich"));
		b2 = bookDao.save(b2);

		// Create Instances for the book
		Borrowable<Media> bor1 = new Borrowable<>("a", true, b1);
		Borrowable<Media> bor2 = new Borrowable<>("b", true, b2);

		bor1 = borrowableDao.save(bor1);
		bor2 = borrowableDao.save(bor2);

		List<Borrowable<Media>> bookinstances = Arrays.asList(bor1, bor2);

		Customer c = new Customer("hirsch", "wald");
		c = customerDao.save(c);

		LocalDateTime loanDate = LocalDateTime.now();
		LocalDateTime dueDate = loanDate.plusMonths(1);

		// create
		Loan l = new Loan(loanDate, dueDate, c, bookinstances);
		l = loanDao.save(l);

		assertTrue(loanDao.findById(l.getId()).isPresent());

		// update
		Borrowable<Media> bor3 = new Borrowable<>("c", true, b2);
		bor3 = borrowableDao.save(bor3);

		List<Borrowable<Media>> moreBooks = Arrays.asList(bor3);
		Loan lToUpdate = new Loan(l.getId(), l.getLoanDate(), l.getDueDate(), l.getCustomer(), moreBooks);
		lToUpdate = loanDao.save(lToUpdate);

		// read
		Loan loanFound = loanDao.findById(lToUpdate.getId()).get();
		assertEquals(lToUpdate, loanFound);

		// find
		List<Loan> loansForC = loanDao.findByCustomer(c);
		assertEquals(1, loansForC.size());
		assertTrue(loansForC.get(0).contains(bor3));
		assertFalse(loansForC.get(0).contains(bor1));

		// destroy
		loanDao.delete(lToUpdate);
		assertFalse(loanDao.findById(lToUpdate.getId()).isPresent());
		// Idempotence => No exception
		loanDao.delete(lToUpdate);
	}

}
