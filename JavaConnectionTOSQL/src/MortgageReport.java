import java.text.NumberFormat;
import java.util.ArrayList;

public class MortgageReport {
	private final ArrayList<String> payments = new ArrayList<>();
    private final Calculator calculator;
    private final NumberFormat currency;

    public MortgageReport(Calculator calculator) {
        this.calculator = calculator;
        currency = NumberFormat.getCurrencyInstance();
    }
    public void printPaymentSchedule() {
        System.out.println();
        System.out.println("Harmonogram Opłat");
        System.out.println("-----------------");
        for(double balance : calculator.getRemainingBalances()) {
            String formattedBalance = currency.format(balance);
            System.out.println(formattedBalance);
            payments.add(formattedBalance); 
        }
    }
    public ArrayList<String> getPayments(){
    	return payments;
    }
    
    public void printMortgage() {
        double mortgage = calculator.calculateMortgage();
        NumberFormat currency = this.currency;
        String mortgagePln = currency.format(mortgage);
        System.out.println("""
                \nKREDYT HIPOTECZNY
                -----------------
                Miesięczna opłata:
                """ + mortgagePln);
    }
}
