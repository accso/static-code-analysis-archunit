package de.accso.library.statistics;

import de.accso.library.ApplicationConfig;
import de.accso.library.datamanagement.model.*;
import de.accso.library.datamanagement.util.BooksDataLoader;
import de.accso.library.datamanagement.util.CustomerDataLoader;
import de.accso.library.datamanagement.util.MoviesDataLoader;
import de.accso.library.statistics.resulttype.MediaTypeWithCount;
import de.accso.library.testutil.AbstractTestWithDataFromExcelSheet;
import de.accso.library.util.LocalDateTimeGenerator;
import de.accso.library.util.UUIDGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class TestStatistics extends AbstractTestWithDataFromExcelSheet {

	private static final String CUSTOMERS_DATA_FILE = "/testdata/statistics/Customers.xlsx";
	private static final String BOOKS_DATA_FILE = "/testdata/statistics/Books.xlsx";
	private static final String MOVIES_DATA_FILE = "/testdata/statistics/Movies.xlsx";

	private CustomerDataLoader cdl;
	private BooksDataLoader bdl;
	private MoviesDataLoader mdl;

	@Autowired
	private MediaTypeStatistics mediaTypeStatistics;

	@Before
	public void setUp() {
		cdl = new CustomerDataLoader(customerDao);
		bdl = new BooksDataLoader(bookDao);
		mdl = new MoviesDataLoader(movieDao);
	}

	/**
	 * Tests the method
	 * {@link MediaTypeStatistics#determineMediaTypesByLoansForYear(int)}.
	 */
	@Test
	public void testRankMediaTypes() throws IOException {
		// Given
		insertDataFor_testRankMediaTypes_IntoDatabase();

		// When
		List<MediaTypeWithCount> computedStatistics = mediaTypeStatistics.determineMediaTypesByLoansForYear(2021);

		// Then
		// @formatter:off
		List<MediaTypeWithCount> expectedStatistics = List.of(
			new MediaTypeWithCount(MediaType.BOOK, 360L),
			new MediaTypeWithCount(MediaType.MOVIE_ON_BLURAY, 270L),
			new MediaTypeWithCount(MediaType.MOVIE_ON_DVD, 90L),
			new MediaTypeWithCount(MediaType.MUSIC, 0L)
		);
		// @formatter:on
		for (int i = 0; i < computedStatistics.size(); i++) {
			assertEquals(expectedStatistics.get(i), computedStatistics.get(i));
		}
	}

	private void insertDataFor_testRankMediaTypes_IntoDatabase() throws IOException {
		cdl.loadDataFromExcelSheet(CUSTOMERS_DATA_FILE);
		bdl.loadDataFromExcelSheet(BOOKS_DATA_FILE);
		mdl.loadDataFromExcelSheet(MOVIES_DATA_FILE);

		List<Media> allMedia = mediaDao.findAll();
		List<Borrowable<Media>> insertedBorrowables = insertNBorrowablesPerMedium(10, allMedia);
		insertNLoansPerBorrowableAndYear(insertedBorrowables, 2019, 5);
		insertNLoansPerBorrowableAndYear(insertedBorrowables, 2020, 7);
		insertNLoansPerBorrowableAndYear(insertedBorrowables, 2021, 9);
	}

	/**
	 * For each <code>medium</code> in the given <code>media</code>, this function
	 * inserts <code>n</code> available {@link Borrowable}s into the database.
	 * 
	 * @return Returns all {@link Borrowable}s that were inserted into the database.
	 */
	private List<Borrowable<Media>> insertNBorrowablesPerMedium(int n, Iterable<Media> media) {
		List<Borrowable<Media>> insertedBorrowables = new ArrayList<>();

		media.forEach(m -> {
			for (int i = 0; i < n; i++) {
				Borrowable<Media> borrowable = new Borrowable<>(UUIDGenerator.getUUID().toString(), true, m);
				borrowableDao.save(borrowable);
				insertedBorrowables.add(borrowable);
			}
		});

		return insertedBorrowables;
	}

	/**
	 * Inserts for each borrawable in <code>borrowables</code> for the given
	 * <code>year</code> exactly <code>n</code> loans into the database.
	 * <p>
	 * Loan information is generated randomly for each new loan.
	 */
	private void insertNLoansPerBorrowableAndYear(Iterable<Borrowable<Media>> borrowables, int year, int n) {
		LocalDateTimeGenerator dateGenerator = LocalDateTimeGenerator.getInstance();
		List<Customer> allCustomers = customerDao.findAll();

		borrowables.forEach(b -> {
			for (int i = 0; i < n; i++) {
				Customer customer = allCustomers.get(n % allCustomers.size());
				LocalDateTime loanDate = dateGenerator.randomDateTimeForYear(year);
				Loan loan = new Loan(loanDate, loanDate.plusWeeks(3), customer, List.of(b));
				loanDao.save(loan);
			}
		});
	}

}
