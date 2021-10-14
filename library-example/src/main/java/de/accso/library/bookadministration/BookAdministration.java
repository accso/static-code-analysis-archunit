package de.accso.library.bookadministration;

import de.accso.library.datamanagement.model.Book;

public interface BookAdministration {

	/**
	 * Ein neues Buch in der Bibliothek anlegen
	 */
	public void registerNewBook(Book book);

	/**
	 * Verleihbare Exemplare zu einem Buch anlegen oder hinzufügen.
	 * <p/>
	 * Vorbedingung: Das Buch selber ist registiert.
	 *
	 * @param book              Das Buch, zu dem verleihbare Exemplare hinzugefügt
	 *                          werden.
	 * @param numberOfInstances Anzahl der Exemplare (muss >= 0 sein).
	 */
	public void registerInstancesForBook(Book book, int numberOfInstances);

	/**
	 * Ein Buch ausrangieren. Dabei wird das Buch selber und alle Instanzen eines
	 * Buches gelöscht.
	 *
	 * @param title Titel (als fachlicher Schlüssel)
	 */
	public void unregisterBook(String title);

}