package de.accso.library.datamanagement.util;

import de.accso.library.datamanagement.manager.CustomerDao;
import de.accso.library.datamanagement.model.Customer;
import org.apache.poi.ss.usermodel.Row;

public class CustomerDataLoader extends AbstractDataLoader<Customer> {

	public CustomerDataLoader(CustomerDao customerDao) {
		super(customerDao);
	}

	@Override
	protected Customer createEntity(Row row) {
		Customer customer = new Customer(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
		return customer;
	}
}
