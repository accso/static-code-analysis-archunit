package de.accso.library.common;

import de.accso.library.datamanagement.model.Customer;
import de.accso.library.datamanagement.model.MediaType;

public interface Authorization {

	/**
	 * Prüft, ob ein Kunde Medien eines bestimmten Medientyps ausleihen darf.
	 *
	 * @param customer
	 * @param mediaType
	 * @return
	 */
	public boolean isCustomerAuthorizedToBorrow(Customer customer, MediaType mediaType);
}
