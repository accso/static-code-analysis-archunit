package de.accso.library.datamanagement.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

/**
 * Book entity class.
 * <p/>
 * Der primary key ist die ID, der business key ist der Titel.
 */
@Entity
public class Book extends Media {

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> authors;

	protected Book() {
		// default constructor for JPA
		super();
	}

	/**
	 *
	 * @param title
	 * @param authors
	 */
	public Book(Integer id, String title, List<String> authors) {
		super(id, title);
		this.authors = authors;
	}

	/**
	 *
	 * @param title
	 * @param authors
	 */
	public Book(String title, List<String> authors) {
		this(null, title, authors);
	}

	@Override
	public String getTitle() {
		return title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	@Override
	public Book clone() {
		return new Book(id, title, new ArrayList<>(authors));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
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
		Book other = (Book) obj;
		if (authors == null) {
			if (other.authors != null)
				return false;
		} else if (!new ArrayList<>(authors).equals(new ArrayList<>(other.authors)))
			return false;
		return true;
	}
}
