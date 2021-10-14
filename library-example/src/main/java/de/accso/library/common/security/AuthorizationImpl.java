package de.accso.library.common.security;

import de.accso.library.common.Authorization;
import de.accso.library.datamanagement.manager.CustomerDao;
import de.accso.library.datamanagement.model.Customer;
import de.accso.library.datamanagement.model.CustomerAccounting;
import de.accso.library.datamanagement.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthorizationImpl implements Authorization {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationImpl.class);

	@Autowired
	CustomerDao customerDao;

	/**
	 * Authorized means:
	 * <ul>
	 * <li>customer is registered</li>
	 * <li>fee is payed for that type.</li>
	 * </ul>
	 */
	@Override
	public boolean isCustomerAuthorizedToBorrow(Customer customer, MediaType mediaType) {

		Customer customerPersistent = customerDao.findByName(customer.getName());
		if (customerPersistent == null) {
			LOGGER.info("Customer '" + customer + "' not authorized because she does not exist in library");
			return false;
		}

		if (mediaType.equals(MediaType.BOOK)) {
			return true;
		}

		CustomerAccounting customerAcc = customerPersistent.getAccounting();
		if (customerAcc == null) {
			// has not payed anything yet.
			LOGGER.info("Customer '" + customer + "' not authorized because she has not payed any fees");
			return false;
		}

		LocalDateTime feePayedUntil = customerAcc.getFeePayedUntilFor(mediaType);

		boolean feePayedUntilLongEnough = feePayedUntil.isAfter(LocalDateTime.now());
		if (!feePayedUntilLongEnough) {
			LOGGER.info("Customer '" + customer + "' not authorized because fee only payed until " + feePayedUntil);
		}
		return feePayedUntilLongEnough;
	}

}
