package de.accso.library.datamanagement.manager;

import de.accso.library.datamanagement.model.Customer;
import de.accso.library.datamanagement.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanDao extends JpaRepository<Loan, Long>, EntityDao<Loan> {

	/**
	 * Searches for the loan with the given id.
	 *
	 * @param id unique id as key.
	 * @return {@code Loan} entity with the specified id, or {@link Optional#empty()} if no
	 *         entity found.
	 */
	@Override
	Optional<Loan> findById(Long id);

	List<Loan> findByCustomer(Customer customer);
}
