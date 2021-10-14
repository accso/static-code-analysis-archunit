package de.accso.library.datamanagement.manager;

import java.util.Collection;

/**
 *
 * Common operations an all entities
 *
 * @param <E>
 */
public interface EntityDao<E> {

	/**
	 * Makes an entity instance persistent or modifies the corresponding persistent entity in storage.
	 *
	 * @param entity (new or detached) entity instance with current data.
	 * @return The persistent (managed) entity (including an ID if new instance).
	 */
	E save(E entity);

	/**
	 * Retrieves all entities of the managed type from storage.
	 *
	 * @return A list of all entities, empty (not {@code null}) if none are found.
	 */
	Collection<E> findAll();

	/**
	 * Removes the given entity instance from storage.
	 *
	 * @param entity (managed or detached) entity instance to be deleted.
	 */
	void delete(E entity);

	/**
	 * Removes all entities from storage.
	 */
	void deleteAll();
}
