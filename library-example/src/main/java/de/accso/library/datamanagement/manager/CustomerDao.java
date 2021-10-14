package de.accso.library.datamanagement.manager;

import de.accso.library.datamanagement.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer, Integer>,  EntityDao<Customer> {

	Customer findByName(String name);

}
