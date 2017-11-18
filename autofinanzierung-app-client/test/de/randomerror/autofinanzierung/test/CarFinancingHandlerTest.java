package de.randomerror.autofinanzierung.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.randomerror.autofinanzierung.ServiceHandlerImpl;

public class CarFinancingHandlerTest {
	
	private static ServiceHandlerImpl serviceHandler;
	
	@BeforeClass
	public static void before() throws Exception {
		serviceHandler = (ServiceHandlerImpl) ServiceHandlerImpl.getInstance();
	}

	
	@Test
	public void testInterestRate() {
		assertEquals(1.0, serviceHandler.getInterestRate(), 0.01);
	}
	
	@Test
	public void testNetLoanAmount() {
		assertEquals(12000.0, serviceHandler.computeNetLoanAmount(14500.0, 2500.0), 0.01);
	}
	
	@Test
	public void testGrossLoanAmount() {
		assertEquals(12065.0, serviceHandler.computeGrossLoanAmount(14500.0, 2500.0, 12), 0.01);
	}
	
	@Test
	public void testMonthlyPayment() {
		assertEquals(1005.42, serviceHandler.computeMonthlyPayment(14500.0, 2500.0, 12), 0.01);
	}
}
