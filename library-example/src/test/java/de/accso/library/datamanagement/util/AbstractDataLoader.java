package de.accso.library.datamanagement.util;

import de.accso.library.datamanagement.manager.EntityDao;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractDataLoader<E> {

	private final EntityDao<E> dao;

	public AbstractDataLoader(EntityDao<E> dao) {
		this.dao = dao;
	}

	public void loadDataFromExcelSheet(String dataResource) throws IOException {
		InputStream dataInputStream = this.getClass().getResourceAsStream(dataResource);
		if (dataInputStream == null) {
			throw new FileNotFoundException("Resource '" + dataResource + "' nicht gefunden");
		}

		// Create Workbook instance holding reference to .xlsx file
		try (Workbook workbook = new XSSFWorkbook(dataInputStream)) {
			// Iterate through each row of first/desired sheet from the workbook
			for (Row row : workbook.getSheetAt(0)) {
				E entity = createEntity(row);
				if (entity != null) {
					dao.save(entity);
				}
			}
		}
	}

	protected abstract E createEntity(Row row);
}
