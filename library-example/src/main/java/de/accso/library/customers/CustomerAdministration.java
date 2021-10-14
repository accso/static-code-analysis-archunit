package de.accso.library.customers;

import de.accso.library.datamanagement.model.Customer;

public interface CustomerAdministration {

	/**
	 * Einen neuen Kundern registrieren
	 * 
	 * @param customer
	 * @throws CustomerBusinessException falls der Kunde bereits registriert ist
	 */
	public void register(Customer customer);

	/**
	 * Einen Kunden deregistrieren
	 * 
	 * @param name
	 * @throws CustomerBusinessException falls der Kunde nicht registriert ist
	 */
	public void unregister(String name);

}
