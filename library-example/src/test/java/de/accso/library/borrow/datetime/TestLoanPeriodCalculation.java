package de.accso.library.borrow.datetime;

import de.accso.library.borrow.impl.BorrowProperties;
import de.accso.library.datamanagement.model.Book;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test class - to find out how Period works exactly...
 */
public class TestLoanPeriodCalculation {

	@Test
	public void testPeriod() {
		Period loanPeriod = BorrowProperties.getBorrowPropertiesFor(new Book("any", Arrays.asList("somebody")))
				.getLoanPeriod();

		assertEquals(1, loanPeriod.getMonths());
		assertEquals(0, loanPeriod.getDays());

		Period testPeriod1 = Period.ofWeeks(3);
		assertEquals(21, testPeriod1.getDays());

		Period testPeriod2 = Period.ofWeeks(5);
		assertEquals(35, testPeriod2.getDays());
		assertEquals(0, testPeriod2.getMonths());
	}

	@Test
	public void testDueDateCalculation() {
		LocalDateTime dueDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		dueDate.plusDays(1);
		assertEquals(0, dueDate.getHour());
		assertEquals(0, dueDate.getMinute());
		assertEquals(0, dueDate.getSecond());
	}

}
