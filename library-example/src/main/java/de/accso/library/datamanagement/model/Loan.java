package de.accso.library.datamanagement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Loan entity class.
 * <p/>
 * Primary key ist die ID. Sie hat keine fachliche Aussagekraft.
 */
@Entity
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime loanDate;
	private LocalDateTime dueDate;

	@ManyToOne(optional = false)
	private Customer customer;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(inverseJoinColumns = @JoinColumn(name = "BORROWABLE_SIGNATURE"))
	private List<Borrowable<Media>> mediaInstancesInLoan;

	protected Loan() {
		// default constructor for JPA
	}

	/**
	 *
	 * @param id                    primary key - must not be null.
	 * @param loanDate
	 * @param dueDate
	 * @param customer
	 * @param borrowedBookInstances
	 */
	public Loan(Long id, LocalDateTime loanDate, LocalDateTime dueDate, Customer customer,
			List<Borrowable<Media>> borrowedBookInstances) {
		requireNonNull(customer, "customer of loan must not be null");
		requireNonNull(borrowedBookInstances, "borrowedBookInstances of loan must not be null");
		this.id = id;
		this.loanDate = loanDate;
		this.dueDate = dueDate;
		this.customer = customer;
		this.mediaInstancesInLoan = borrowedBookInstances;
	}

	/**
	 *
	 * @param loanDate
	 * @param dueDate
	 * @param customer
	 * @param borrowedBookInstances
	 */
	public Loan(LocalDateTime loanDate, LocalDateTime dueDate, Customer customer,
			List<Borrowable<Media>> borrowedBookInstances) {
		this(null, loanDate, dueDate, customer, borrowedBookInstances);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public LocalDateTime getLoanDate() {
		return loanDate;
	}

	public List<Borrowable<Media>> getMediaInstancesInLoan() {
		return mediaInstancesInLoan;
	}

	// convenience
	public boolean contains(Borrowable<Media> bi) {
		return mediaInstancesInLoan.contains(bi);
	}

	/**
	 * This method removes this bookinstance from the loan.
	 *
	 * @param book
	 */
	public void remove(Borrowable<Media> bi) {

		// Careful: Might differ in isAvailable-Flag
		boolean contains = false;
		Borrowable<Media> bToRemove = null;

		for (Borrowable<Media> bPersistent : mediaInstancesInLoan) {
			if (bPersistent.getSignature().equals(bi.getSignature())) {
				contains = true;
				bToRemove = bPersistent;
			}
		}

		if (contains) {
			mediaInstancesInLoan.remove(bToRemove);
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * This is a deep clone. Also the depending customer and book is cloned.
	 */
	@Override
	public Loan clone() {
		final List<Borrowable<Media>> mediaInstancesAsClone = new ArrayList<>();
		mediaInstancesInLoan.forEach(b -> mediaInstancesAsClone.add(b.clone()));
		// LocalDateTime is immutable. Cloning not necessary.
		return new Loan(id, loanDate, dueDate, customer.clone(), mediaInstancesAsClone);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mediaInstancesInLoan == null) ? 0 : mediaInstancesInLoan.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((loanDate == null) ? 0 : loanDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Loan other = (Loan) obj;
		if (mediaInstancesInLoan == null) {
			if (other.mediaInstancesInLoan != null)
				return false;
		} else if (!Arrays.equals(mediaInstancesInLoan.toArray(), other.mediaInstancesInLoan.toArray()))
			// PersistentBag.equals() erfüllt nicht List.equals() API
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (loanDate == null) {
			if (other.loanDate != null)
				return false;
		} else if (!loanDate.equals(other.loanDate))
			return false;
		return true;
	}

}
