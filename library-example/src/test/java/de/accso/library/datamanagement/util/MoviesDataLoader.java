package de.accso.library.datamanagement.util;

import de.accso.library.datamanagement.manager.MovieDao;
import de.accso.library.datamanagement.model.Movie;
import de.accso.library.datamanagement.model.StorageType;
import org.apache.poi.ss.usermodel.Row;

public class MoviesDataLoader extends AbstractDataLoader<Movie> {

	public MoviesDataLoader(MovieDao movieDao) {
		super(movieDao);
	}

	@Override
	protected Movie createEntity(Row row) {
		String title = row.getCell(0).getStringCellValue();
		StorageType storageType = StorageType.valueOf(row.getCell(1).getStringCellValue());
		Movie movie = new Movie(title, "(Unbekannt)", storageType);
		return movie;
	}
	
}
