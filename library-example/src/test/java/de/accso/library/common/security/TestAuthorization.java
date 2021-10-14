package de.accso.library.common.security;

import de.accso.library.common.Authorization;
import de.accso.library.datamanagement.model.Customer;
import de.accso.library.datamanagement.model.CustomerAccounting;
import de.accso.library.datamanagement.model.MediaType;
import de.accso.library.testutil.AbstractTestWithDataFromExcelSheet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestAuthorization extends AbstractTestWithDataFromExcelSheet {

	@Autowired
	private Authorization auth;

	@Test
	public void testIsCustomerAuthorizedToBorrowBooksOK() {
		Customer c = customerDao.findByName("Paul");
		CustomerAccounting customerAcc = new CustomerAccounting();

		customerAcc.payFeeUntil(MediaType.MUSIC, LocalDateTime.now().plusDays(2));
		customerAcc.payFeeUntil(MediaType.MOVIE_ON_DVD, LocalDateTime.now().minusDays(2));

		c.setAccounting(customerAcc);
		c = customerDao.save(c);

		boolean isAuthorized = auth.isCustomerAuthorizedToBorrow(c, MediaType.BOOK);
		assertTrue("Paul should be authorized", isAuthorized);

		isAuthorized = auth.isCustomerAuthorizedToBorrow(c, MediaType.MUSIC);
		assertTrue("Paul should be authorized", isAuthorized);
	}

	@Test
	public void testNOKCustomerNotRegistered() {
		Customer c = new Customer("Hinz und Kunz", "Im Hier und Jetzt");

		boolean isAuthorized = auth.isCustomerAuthorizedToBorrow(c, MediaType.MOVIE_ON_BLURAY);
		assertFalse("Hinz und Kunz should not be authorized", isAuthorized);
	}

	@Test
	public void testNOKCustomerHasNotPayedForMT() {
		Customer c = customerDao.findByName("Paul");
		CustomerAccounting customerAcc = new CustomerAccounting();
		customerAcc.payFeeUntil(MediaType.MUSIC, LocalDateTime.now().plusDays(1));

		c.setAccounting(customerAcc);
		c = customerDao.save(c);

		boolean isAuthorized = auth.isCustomerAuthorizedToBorrow(c, MediaType.MOVIE_ON_BLURAY);
		assertFalse(c + " should not be authorized", isAuthorized);
	}

	@Test
	public void testNOKCustomerPaymentTooLongAgo() {
		Customer c = customerDao.findByName("Paul");
		CustomerAccounting customerAcc = new CustomerAccounting();
		customerAcc.payFeeUntil(MediaType.MUSIC, LocalDateTime.now().minusDays(2));

		c.setAccounting(customerAcc);
		c = customerDao.save(c);

		boolean isAuthorized = auth.isCustomerAuthorizedToBorrow(c, MediaType.MUSIC);
		assertFalse(c + " should not be authorized", isAuthorized);
	}

}
