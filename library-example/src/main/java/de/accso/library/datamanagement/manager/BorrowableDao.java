package de.accso.library.datamanagement.manager;

import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BorrowableDao extends JpaRepository<Borrowable<Media>, String> {

	/**
	 * Gets all instances of this media
	 *
	 * @param media
	 * @return
	 */
	public Set<Borrowable<Media>> findByMedia(Media media);

	/**
	 * Removes all instances for this media
	 *
	 * @param media
	 */
	public void removeByMedia(Media media);

}
