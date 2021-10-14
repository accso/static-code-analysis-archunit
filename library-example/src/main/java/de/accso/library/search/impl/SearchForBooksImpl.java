package de.accso.library.search.impl;

import de.accso.library.datamanagement.manager.BookDao;
import de.accso.library.datamanagement.model.Book;
import de.accso.library.search.SearchForBooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SearchForBooksImpl implements SearchForBooks {

	@Autowired
	private BookDao bookDao;

	@Override
	public Collection<Book> findBooksByAuthor(String author) {
		return bookDao.findByAuthorsContaining(author);
	}
}
