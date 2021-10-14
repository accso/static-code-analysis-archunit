package de.accso.library.bookadministration.impl;

import de.accso.library.bookadministration.BookAdministration;
import de.accso.library.datamanagement.manager.BookDao;
import de.accso.library.datamanagement.manager.BorrowableDao;
import de.accso.library.datamanagement.model.Book;
import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Media;
import de.accso.library.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BookAdministrationImpl implements BookAdministration {

	@Autowired
	private BookDao bookDao;

	@Autowired
	private BorrowableDao borrowableDao;

	@Override
	public void registerNewBook(Book book) {
		if (bookDao.findByTitle(book.getTitle()) == null) {
			bookDao.save(book);
		}
	}

	@Override
	public void unregisterBook(String title) {
		Book media = bookDao.findByTitle(title);
		if (media != null) {
			Set<Borrowable<Media>> borrowables = borrowableDao.findByMedia(media);
			borrowableDao.deleteAll(borrowables);
			bookDao.delete(bookDao.findByTitle(title));
		}
	}

	@Override
	public void registerInstancesForBook(Book book, int numberOfInstances) {
		for (int i = 1; i <= numberOfInstances; i++) {
			Borrowable<Media> instance = new Borrowable<>(UUIDGenerator.getUUID().toString(), true, book);
			borrowableDao.save(instance);
		}
	}

}
