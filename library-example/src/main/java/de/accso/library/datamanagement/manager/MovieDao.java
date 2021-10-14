package de.accso.library.datamanagement.manager;

import de.accso.library.datamanagement.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDao extends JpaRepository<Movie, Integer>, EntityDao<Movie> {

	/**
	 * Find movie by title (business key).
	 *
	 * @param title movie title to search for
	 * @return null if not found
	 */
	Movie findByTitle(String title);

}
