package de.accso.library.datamanagement.manager;

import de.accso.library.datamanagement.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaDao extends JpaRepository<Media, Integer> {

	/**
	 * Find media by title (business key).
	 *
	 * @param title media title to search for
	 * @return null if not found
	 */
	Media findByTitle(String title);

}
