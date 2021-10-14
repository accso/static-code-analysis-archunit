package de.accso.library.bookadministration;

import de.accso.library.ApplicationConfig;
import de.accso.library.datamanagement.manager.BookDao;
import de.accso.library.datamanagement.manager.BorrowableDao;
import de.accso.library.datamanagement.model.Book;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class TestBookAdministration {

	@Autowired
	private BookDao bookDao;

	@Autowired
	private BorrowableDao borrowableDao;

	@Autowired
	private BookAdministration bookAdmin;

	@After
	public void tearDown() throws Exception {
		bookDao.deleteAll();
	}

	@Test
	public void testRegisterAndUnregisterOk() {
		String title = "Ein neues Buch";
		Book newBook = createNewBookWithTitle(title);

		bookAdmin.registerNewBook(newBook);
		assertEquals(newBook, bookDao.findByTitle(title));

		bookAdmin.registerInstancesForBook(newBook, 3);
		bookAdmin.registerInstancesForBook(newBook, 5);
		assertEquals(8, borrowableDao.findByMedia(newBook).size());

		bookAdmin.unregisterBook(title);

		assertNull("Book should not exist anymore", bookDao.findByTitle(title));

	}

	private Book createNewBookWithTitle(String title) {
		List<String> authors = new ArrayList<>();
		authors.add("Autor1");
		return new Book(title, authors);
	}

}
