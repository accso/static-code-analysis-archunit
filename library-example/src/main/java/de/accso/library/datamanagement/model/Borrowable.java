package de.accso.library.datamanagement.model;

import de.accso.library.borrow.BorrowableMediaState;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity class representing the physical instances of any {@link Media}, which may be borrowed from the library.
 * Each instance is identified by its unique {@link #signature} (primary key).
 *
 * @param <T> concrete type of {@link Media}.
 */
@Entity
public class Borrowable<T extends Media> {

	@Id
	private String signature;

	private boolean isAvailable;
	private boolean isReserved;

	@ManyToOne(targetEntity = Media.class, optional = false)
	private T media;

	protected Borrowable() {
		// default constructor for JPA
	}

	public Borrowable(String signature, boolean isAvailable, T media) {
		this(signature, isAvailable, false, media);
	}

	private Borrowable(String signature, boolean isAvailable, boolean isReserved, T media) {
		this.signature = signature;
		this.isAvailable = isAvailable;
		this.isReserved = isReserved;
		this.media = media;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public String getSignature() {
		return signature;
	}

	public T getMedia() {
		return media;
	}

	/**
	 * Determines the {@link BorrowableMediaState state} instance corresponding to
	 * this media.
	 *
	 * @return the {@link BorrowableMediaState} enum instance determined from
	 *         {@link #isAvailable} and {@link #isReserved}.
	 */
	public BorrowableMediaState getMediaState() {
		if (isAvailable) {
			if (isReserved) {
				throw new IllegalStateException("A Media cannot be available and reserved simultaneously!");
			} else { // !isReserved
				return BorrowableMediaState.AVAILABLE;
			}
		} else { // !isAvailable
			if (isReserved)
				return BorrowableMediaState.RESERVED;
			else { // !isReserved
				return BorrowableMediaState.BORROWED;
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Borrowable<T> clone() {
		return new Borrowable<>(signature, isAvailable, isReserved, (T) media.clone());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAvailable ? 1231 : 1237);
		result = prime * result + (isReserved ? 1231 : 1237);
		result = prime * result + ((media == null) ? 0 : media.hashCode());
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
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
		Borrowable<?> other = (Borrowable<?>) obj;
		if (isAvailable != other.isAvailable)
			return false;
		if (isReserved != other.isReserved)
			return false;
		if (media == null) {
			if (other.media != null)
				return false;
		} else if (!media.equals(other.media))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("signature", signature)
				.append("isAvailable", isAvailable)
				.append("isReserved", isReserved)
				.append("media", media.toStringWithoutInstances()) // ToString-Zyklus vermeiden: Media.instances.values -> Borrowable
				.toString();
	}

}
