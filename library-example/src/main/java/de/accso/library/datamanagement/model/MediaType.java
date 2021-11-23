package de.accso.library.datamanagement.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum MediaType {
	BOOK, MUSIC, MOVIE_ON_DVD, MOVIE_ON_BLURAY;

	private static final Map<Class<? extends Media>, Function<Media, MediaType>> mediaTypeMap = new HashMap<>();

	static {
		mediaTypeMap.put(Book.class, media -> BOOK);
		mediaTypeMap.put(Movie.class, media -> getMovieMediaType(media));
	}

	public static MediaType getMediaTypeFor(Media media) {
		Function<Media, MediaType> mediaTypeFunction = mediaTypeMap.get(media.getClass());
		return mediaTypeFunction != null ? mediaTypeFunction.apply(media) : null;
	}

	private static MediaType getMovieMediaType(Media media) {
		// For movies, we have to differentiate - if DVD or Blu-Ray.
		Movie movie = (Movie) media;

		switch (movie.getStorageType()) {
		case DVD:
			return MOVIE_ON_DVD;
		case BLURAY:
			return MOVIE_ON_BLURAY;
		default:
			return null;
		}
	}

}
