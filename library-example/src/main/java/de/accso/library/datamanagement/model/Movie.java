package de.accso.library.datamanagement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Movie extends Media {

	private String producer;

	@Enumerated(EnumType.STRING)
	private StorageType storageType;

	protected Movie() {
		// default constructor for JPA
		super();
	}

	public Movie(String title, String producer, StorageType storageType) {
		this(null, title, producer, storageType);
	}

	private Movie(Integer id, String title, String producer, StorageType storageType) {
		super(id, title);
		this.producer = producer;
		this.storageType = storageType;
	}

	public String getProducer() {
		return producer;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	@Override
	public Movie clone() {
		return new Movie(this.id, this.title, this.producer, this.storageType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((producer == null) ? 0 : producer.hashCode());
		result = prime * result + ((storageType == null) ? 0 : storageType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (producer == null) {
			if (other.producer != null)
				return false;
		} else if (!producer.equals(other.producer))
			return false;
		if (storageType != other.storageType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
