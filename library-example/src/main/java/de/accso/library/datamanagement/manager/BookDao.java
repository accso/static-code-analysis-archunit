package de.accso.library.datamanagement.manager;

import de.accso.library.datamanagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BookDao extends JpaRepository<Book, Integer>, EntityDao<Book> {

	/**
	 * Find book by title (business key).
	 *
	 * @param title book title to search for
	 * @return null if not found
	 */
	Book findByTitle(String title);

	/**
	 * Searches for all books with the given author. The search looks for a
	 * partially matching string, ignoring case.
	 *
	 * @param author a (partial) author name to search for.
	 * @return A list of all matching {@code Book}s, empty (not {@code null}) if
	 *         none are found.
	 */
	@Query("SELECT b FROM Book b JOIN b.authors a WHERE a = :author")
	Collection<Book> findByAuthorsContaining(@Param("author") String author);

}
