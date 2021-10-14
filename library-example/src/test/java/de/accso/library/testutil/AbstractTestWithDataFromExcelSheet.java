package de.accso.library.testutil;

import de.accso.library.ApplicationConfig;
import de.accso.library.datamanagement.manager.*;
import de.accso.library.datamanagement.util.BooksDataLoader;
import de.accso.library.datamanagement.util.CustomerDataLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public abstract class AbstractTestWithDataFromExcelSheet {

	@ClassRule
	public static H2ConsoleServerRule consoleServerRule = new H2ConsoleServerRule();

	private final String customerDataFile = "/testdata/release1/Customer.xlsx";
	private final String bookDataFile = "/testdata/release1/Book.xlsx";

	@Autowired
	protected CustomerDao customerDao;

	@Autowired
	protected BookDao bookDao;

	@Autowired
	protected LoanDao loanDao;

	@Autowired
	protected BorrowableDao borrowableDao;

	@Autowired
	protected MovieDao movieDao;

	@Autowired
	protected MediaDao mediaDao;

	@Before
	public void setUp() throws IOException {
		CustomerDataLoader cdl = new CustomerDataLoader(customerDao);
		BooksDataLoader bdl = new BooksDataLoader(bookDao);

		// use test data from excel sheets
		cdl.loadDataFromExcelSheet(customerDataFile);
		bdl.loadDataFromExcelSheet(bookDataFile);
	}

	@After
	public void tearDown() throws Exception {
		loanDao.deleteAll();
		customerDao.deleteAll();
		borrowableDao.deleteAll();
		bookDao.deleteAll();
		mediaDao.deleteAll();
	}
}
