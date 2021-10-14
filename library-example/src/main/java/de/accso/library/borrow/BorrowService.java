package de.accso.library.borrow;

import de.accso.library.datamanagement.model.Borrowable;
import de.accso.library.datamanagement.model.Customer;
import de.accso.library.datamanagement.model.Loan;
import de.accso.library.datamanagement.model.Media;

import java.util.List;

/**
 * Alle Use cases rund um den Verleih von Medien
 */
public interface BorrowService {

	/**
	 * Medien ausleihen.
	 * <p>
	 * Vorbedingungen:
	 * <ul>
	 * <li>Nur ein registrierter Kunde kann Medien ausleihen.
	 * <li>Nur Medien, die auf dem Lager sind, d.h. insbesondere nicht verliehen
	 * sind, können ausgeliehen werden.
	 * </ul>
	 * Nachbedingungen:
	 * <ul>
	 * <li>Alle Medien, die ausgeliehen wurden (d.h. Teil des Loan sind), sind nicht
	 * mehr verfügbar.
	 * </ul>
	 *
	 * @param media
	 * @param customer
	 * @return Liste der Loans: Da es verschiedene Leihfristen gibt, gibt es
	 *         verschiedenen Loans für verschiedene Leihfristen. Wenn kein Medium
	 * @throws BorrowBusinessException wenn der Kunde nicht autorisiert war oder
	 *                                 eines der auszuleihenden Medien nicht in der
	 *                                 Bibliothek exisitiert (wenn Medien nicht
	 *                                 ausgeliehen werden konnten, weil sie bereits
	 *                                 verliehen sind, wird dagegen <i>keine</i>
	 *                                 Exception geworfen).
	 */
	public <T extends Media> List<Loan> borrow(List<T> media, Customer customer) throws BorrowBusinessException;

	/**
	 * Medien zurückgeben. Es können beliebige Medien zurückgegeben werden, d.h. es
	 * muss nicht ein Loan vollständig zurückgegeben werden, außerdem können Medien
	 * aus verschiedenen Loans zusammen zurückgegeben werden. Es ist auch möglich,
	 * Medien zurückzugeben, die zwischenzeitlich deregsitiert wurden, oder Medien
	 * für Kunden zurückzugeben, die sich zwischenzeitlich deregistriert haben.
	 * Nachbedingungen:
	 * <ul>
	 * <li>Alle Medien, die zurückgegeben wurden, sind wieder verfügbar.
	 * </ul>
	 *
	 * @param instancesToReturn
	 * @param customer
	 */
	public void returnLoans(List<Borrowable<Media>> instancesToReturn, Customer customer);

}
