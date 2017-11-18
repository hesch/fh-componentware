package de.randomerror.autofinanzierung;

public interface CarFinancingHandler {
	public double computeGrossLoanAmount(double price, double firstInstallment, int paymentTerm);
	public double computeMonthlyPayment(double price, double firstInstallment, int paymentTerm);
	public double computeNetLoanAmount(double price, double firstInstallment);
	public double getInterestRate();
}
