package de.accso.library.statistics.resulttype;

import de.accso.library.datamanagement.model.MediaType;

/**
 * A class which provides immutable count information for {@link MediaType}s.
 */
public class MediaTypeWithCount {

	private MediaType mediaType;
	private Long count;

	public MediaTypeWithCount(MediaType mediaType, Long count) {
		this.mediaType = mediaType;
		this.count = count;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public Long getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "MediaTypeWithCount [mediaType=" + mediaType + ", count=" + count + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((mediaType == null) ? 0 : mediaType.hashCode());
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
		MediaTypeWithCount other = (MediaTypeWithCount) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (mediaType != other.mediaType)
			return false;
		return true;
	}

}
