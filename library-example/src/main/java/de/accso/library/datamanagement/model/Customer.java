package de.accso.library.datamanagement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

/**
 * Customer entity class.
 * <p/>
 * Der primary key ist die ID, der business key ist der Name.
 */
@Entity
public class Customer {

	// primary key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String name;
	private String address;

	@Embedded
	private CustomerAccounting accounting;

	protected Customer() {
		// default constructor for JPA
	}

	/**
	 * @param name   Business key. Must not be null
	 * @param address
	 */
	public Customer(String name, String address) {
		this(null, name, address, new CustomerAccounting());
	}

	private Customer(Integer id, String name, String address, CustomerAccounting accounting) {
		requireNonNull(name, "Name of customer must not be null");
		this.id = id;
		this.name = name;
		this.address = address;
		this.accounting = accounting;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CustomerAccounting getAccounting() {
		return accounting;
	}

	public void setAccounting(CustomerAccounting accounting) {
		this.accounting = accounting;
	}

	@Override
	public Customer clone() {
		return new Customer(id, name, address, accounting);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accounting == null) ? 0 : accounting.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Customer other = (Customer) obj;
		if (accounting == null) {
			if (other.accounting != null)
				return false;
		} else if (!accounting.equals(other.accounting))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
