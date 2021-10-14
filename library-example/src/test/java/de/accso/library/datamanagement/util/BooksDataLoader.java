package de.accso.library.datamanagement.util;

import de.accso.library.datamanagement.manager.BookDao;
import de.accso.library.datamanagement.model.Book;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BooksDataLoader extends AbstractDataLoader<Book> {

	public BooksDataLoader(BookDao bookDao) {
		super(bookDao);
	}

	@Override
	protected Book createEntity(Row row) {
		List<String> authors = new ArrayList<>();
		StringTokenizer autorenZerschneider = new StringTokenizer(row.getCell(1).getStringCellValue(), ":");
		while (autorenZerschneider.hasMoreTokens()) {
			authors.add(autorenZerschneider.nextToken());
		}
		Book book = new Book(row.getCell(0).getStringCellValue(), authors);
		return book;
	}
}
