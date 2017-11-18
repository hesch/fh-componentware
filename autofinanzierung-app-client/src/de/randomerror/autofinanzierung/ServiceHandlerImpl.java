package de.randomerror.autofinanzierung;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.car_financing.client.shared.CarFinancingHandler;
import de.fh_dortmund.inf.cw.car_financing.client.shared.ServiceHandler;

public class ServiceHandlerImpl extends ServiceHandler implements CarFinancingHandler {
	private static ServiceHandler instance = new ServiceHandlerImpl();
	public static ServiceHandler getInstance() {return instance;}

	private Context ctx;
	
	CarFinancingHandlerRemote handler;
	
	private ServiceHandlerImpl() {
		try {
			ctx = new InitialContext();
			
			String beanName = "java:global/autofinanzierungEAR/autofinanzierung/CarFinancingHandlerBean!de.randomerror.autofinanzierung.CarFinancingHandlerRemote";
			handler = (CarFinancingHandlerRemote) ctx.lookup(beanName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	@Override
	public double computeGrossLoanAmount(double price, double firstInstallment, int paymentTerm) {
		return handler.computeGrossLoanAmount(price, firstInstallment, paymentTerm);
	}

	@Override
	public double computeMonthlyPayment(double price, double firstInstallment, int paymentTerm) {
		return handler.computeMonthlyPayment(price, firstInstallment, paymentTerm);
	}

	@Override
	public double computeNetLoanAmount(double price, double firstInstallment) {
		return handler.computeNetLoanAmount(price, firstInstallment);
	}

	@Override
	public double getInterestRate() {
		return handler.getInterestRate();
	}

}
