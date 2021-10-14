package de.accso.library.datamanagement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Abstract parent class for any type of media, like books or movies.
 * Each instance is identified by its unique title (business key) and its technical ID (primary key).
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Immutable
public abstract class Media {

	// primary key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

	@Column(unique = true)
	protected String title;

	@OneToMany(mappedBy = "media", fetch = FetchType.EAGER)
	@MapKey
	protected Map<String, Borrowable<Media>> instances = new HashMap<>();

	protected Media() {
		// default constructor for JPA
	}

	protected Media(Integer id, String title) {
		requireNonNull(title, () -> "Title of " + this.getClass().getSimpleName() + " must not be null");
		this.id = id;
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return this.title;
	}

	public void addInstance(Borrowable<Media> borrowable) {
		if (!instances.containsKey(borrowable.getSignature())) {
			instances.put(borrowable.getSignature(), borrowable);
		}
	}

	public void updateInstance(Borrowable<Media> borrowable) {
		if (instances.containsKey(borrowable.getSignature())) {
			instances.put(borrowable.getSignature(), borrowable);
		}
	}

	public Collection<Borrowable<Media>> getAllInstances() {
		return instances.values();
	}

	/**
	 * set a whole bunch of instances - only those are added that are not yet part
	 * of the media.
	 *
	 * @param instancesToAdd
	 */
	public void setInstances(Set<Borrowable<Media>> instancesToAdd) {
		for (Borrowable<Media> borrowable : instancesToAdd) {
			if (!instancesToAdd.contains(borrowable.getSignature())) {
				instances.put(borrowable.getSignature(), borrowable);
			}
		}
	}

	String toStringWithoutInstances() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", this.id)
				.append("title", this.title)
				.toString();
	}

	@Override
	public abstract Media clone();

	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Media other = (Media) obj;
		return Objects.equals(this.id, other.id) && Objects.equals(this.title, other.title);
	}

}
