package de.accso.library.datamanagement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class CustomerAccounting {

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyEnumerated(EnumType.STRING)
	@MapKeyColumn(length = 32)
	private Map<MediaType, LocalDateTime> feePayedDate = new HashMap<>();

	public void payFeeUntil(MediaType mediaType, LocalDateTime until) {
		feePayedDate.put(mediaType, until);
	}

	public LocalDateTime getFeePayedUntilFor(MediaType mediaType) {
		if (feePayedDate.containsKey(mediaType)) {
			return feePayedDate.get(mediaType);
		}
		return LocalDateTime.MIN;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((feePayedDate == null) ? 0 : feePayedDate.hashCode());
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
		CustomerAccounting other = (CustomerAccounting) obj;
		if (feePayedDate == null) {
			if (other.feePayedDate != null)
				return false;
		} else if (!feePayedDate.equals(other.feePayedDate))
			return false;
		return true;
	}

}
