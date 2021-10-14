package de.accso.library.customers.impl;

import de.accso.library.customers.CustomerAdministration;
import de.accso.library.customers.CustomerBusinessException;
import de.accso.library.datamanagement.manager.CustomerDao;
import de.accso.library.datamanagement.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerAdministrationImpl implements CustomerAdministration {

	@Autowired
	private CustomerDao customerDao;

	@Override
	public void register(Customer customer) {
		if (customerDao.findByName(customer.getName()) != null) {
			throw new CustomerBusinessException("Ccustomer '" + customer + "' already registered");
		}
		customerDao.save(customer);
	}

	@Override
	public void unregister(String name) {
		if (customerDao.findByName(name) == null) {
			throw new CustomerBusinessException("Ccustomer '" + name + "' is not registered");
		}
		customerDao.delete(customerDao.findByName(name));
	}
}
