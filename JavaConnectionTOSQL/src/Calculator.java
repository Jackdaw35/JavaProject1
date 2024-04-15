public class Calculator {
    private static final byte MONTHS = 12;
    private static final byte PERCENT = 100;

    private final int principle;
    private final double annualInterest;
    private final byte years;

    public Calculator(int principle, double annualInterest, byte years) {
        this.principle = principle;
        this.annualInterest = annualInterest;
        this.years = years;
    }
    public double calculateBalance(short paymentsDone) {
        double monthlyInterest = getMonthlyInterest();
        int numberOfPayments = getNumberOfPayments();
        double mortgagePatternNumber = Math.pow(1 + monthlyInterest, numberOfPayments);
        double mortgagePatternDone = Math.pow(1 + monthlyInterest, paymentsDone);
        return principle * (mortgagePatternNumber - mortgagePatternDone) / (mortgagePatternNumber - 1);

    }
    public double calculateMortgage() {
        double monthlyInterest = getMonthlyInterest();
        int numberOfPayments = getNumberOfPayments();
        double mortgagePatternNumber = Math.pow(1 + monthlyInterest, numberOfPayments);
        return principle * (monthlyInterest * mortgagePatternNumber) / (mortgagePatternNumber - 1);
    }

    private double getMonthlyInterest() {
        return annualInterest / PERCENT / MONTHS;
    }
    private int getNumberOfPayments() {
        return years * MONTHS;
    }
    public double[] getRemainingBalances() {
        var balances = new double[getNumberOfPayments()];
        for (short month = 1; month <= balances.length; month++)
            balances[month-1] = calculateBalance(month);
        return balances;
    }
}
