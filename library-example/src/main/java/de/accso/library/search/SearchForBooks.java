package de.accso.library.search;

import de.accso.library.datamanagement.model.Book;

import java.util.Collection;

public interface SearchForBooks {
	/**
	 * Bücher über Autoren suchen
	 * 
	 * @param author
	 * @return Die Bücher, die dieser Autor mitgeschrieben hat
	 */
	public Collection<Book> findBooksByAuthor(String author);

}
