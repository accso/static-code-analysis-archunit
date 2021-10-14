package de.accso.library.customers;

import de.accso.library.ApplicationConfig;
import de.accso.library.datamanagement.manager.CustomerDao;
import de.accso.library.datamanagement.model.Customer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class TestCustomerRegistration {

	@Autowired
	private CustomerAdministration customerAdmin;

	@Autowired
	private CustomerDao customerDao;

	@After
	public void tearDown() throws Exception {
		customerDao.deleteAll();
	}

	@Test
	public void testRegister() {
		Customer cNew = new Customer("Harry", "Hier");
		customerAdmin.register(cNew);
		assertNotNull(customerDao.findByName("Harry"));
		assertEquals(customerDao.findByName("Harry"), cNew);

		// second ry to register this should fail
		try {
			customerAdmin.register(cNew);
			fail("Expected CustomerBusinessException");
		} catch (CustomerBusinessException e) {
		}
	}

	@Test
	public void testUnregister() {
		Customer cNew = new Customer("Harry", "Hier");
		customerAdmin.register(cNew);
		customerAdmin.unregister("Harry");
		assertNull("Customer Harry should not exist anymore!", customerDao.findByName("Harry"));

		// second ry to register this should fail
		try {
			customerAdmin.unregister("Harry");
			fail("Expected CustomerBusinessException");
		} catch (CustomerBusinessException e) {
		}
	}
}
