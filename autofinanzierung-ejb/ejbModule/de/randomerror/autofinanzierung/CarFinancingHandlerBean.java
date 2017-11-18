package de.randomerror.autofinanzierung;

import javax.annotation.Resource;
import javax.ejb.Stateless;

@Stateless
public class CarFinancingHandlerBean implements CarFinancingHandlerLocal, CarFinancingHandlerRemote {

	@Resource(name="interestRate")
	private double interestRate;
	
	@Override
	public double computeGrossLoanAmount(double price, double firstInstallment, int paymentTerm) {
		double loanSum = computeNetLoanAmount(price, firstInstallment);
		double monthlyPayment = loanSum / paymentTerm;
		double interestRate = getInterestRate() / 100;
		
		double interestAccumulator = 0;
		
		for(int i = 0; i < paymentTerm; i++) {
			interestAccumulator += loanSum * interestRate / paymentTerm;
			loanSum -= monthlyPayment;
		}
		
		return computeNetLoanAmount(price, firstInstallment) + interestAccumulator;
	}

	@Override
	public double computeMonthlyPayment(double price, double firstInstallment, int paymentTerm) {
		return computeGrossLoanAmount(price, firstInstallment, paymentTerm) / paymentTerm;
	}

	@Override
	public double computeNetLoanAmount(double price, double firstInstallment) {
		return price-firstInstallment;
	}

	@Override
	public double getInterestRate() {
		return interestRate * 100;
	}

}
