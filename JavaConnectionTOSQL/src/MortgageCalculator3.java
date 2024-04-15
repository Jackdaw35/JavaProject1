import java.sql.*;
import java.util.ArrayList;

public class MortgageCalculator3 {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }

        System.out.println("KALKULATOR KREDYTOWY");
        String fullName = Console.getName();
        Integer age = Console.getAge();
        Integer monthlySalary = Console.getMonthlySalary();
        String Employment = Console.getEmployment();
        int principal = (int) Console.readNumber("Wielkość kredytu (1tys zł  - 1mln zł): ", 1000, 1_000_000);
        double annualInterest = Console.readNumber("Podaj roczną stopę procentową: ", 0, 30);
        byte years = (byte) Console.readNumber("Okres kredytu(w latach): ", 1, 30);
        String firstprincipal = Integer.toString(principal) + ",00 zł";

        var calculator = new Calculator(principal,annualInterest,years);
        var report = new MortgageReport(calculator);
        report.printMortgage();
        report.printPaymentSchedule();
        
        ArrayList<String> payments = report.getPayments();

                // Database connection parameters
                String url = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:5432/postgres";
                String user = "postgres.znziuvtqyyrwgauggtdl";
                String password = "HerbatazRumem12_)";
        
                // SQL commands
                String customerinfo = "INSERT INTO dim_customer (name, age, employment_type, monthlySalary) VALUES (?, ?, ?, ?)";
                String calculationinfo = "INSERT INTO dim_calculation (customerid, mortageamount, interestrate, duration_years) " +
                                         "SELECT a.Id AS CustomerId, ?, ?, ? FROM dim_customer a " +
                                         "LEFT JOIN dim_calculation b ON a.Id = b.CustomerId WHERE b.CustomerId IS NULL";
                
            
                
                String paymentsfact = "INSERT INTO fact_calculations (calculationid, periodid, saldo) " +
                        "SELECT MAX(Id) AS CalculationId, NULL AS periodid, ? FROM dim_calculation;";
                
                String firstpaymentrow = "INSERT INTO fact_calculations (calculationid, periodid, saldo) " +
                        "SELECT MAX(Id) AS CalculationId, NULL AS periodid, ? FROM dim_calculation;";
                
                String sqlLoop = "DO $$ "
                        + "DECLARE "
                        + "    row_record RECORD; "
                        + "    var_orderid INTEGER; "
                        + "    var_startingperiod INTEGER; "
                        + "    var_maxcalculationid INTEGER; "
                        + "BEGIN "
                        + "    SELECT MIN(a.orderid) INTO var_orderid "
                        + "    FROM fact_calculations a "
                        + "    WHERE a.CalculationId = (SELECT MAX(CalculationId) FROM fact_calculations); "
                        + "    SELECT Id - 1 INTO var_startingperiod "
                        + "    FROM dim_periods "
                        + "    WHERE MONTH = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) AND YEAR = EXTRACT(YEAR FROM CURRENT_TIMESTAMP); "
                        + "    SELECT MAX(CalculationId) INTO var_maxcalculationid FROM fact_calculations; "
                        + "    FOR row_record IN SELECT * FROM fact_calculations WHERE OrderId <> var_orderid AND CalculationId = var_maxcalculationid LOOP "
                        + "        UPDATE fact_calculations "
                        + "        SET periodid = var_startingperiod + 1 "
                        + "        WHERE OrderId = row_record.OrderId; "
                        + "        var_startingperiod := var_startingperiod + 1; "
                        + "    END LOOP; "
                        + "END $$;";

                try (Connection connection = DriverManager.getConnection(url, user, password);
                        PreparedStatement insertCustomerStatement = connection.prepareStatement(customerinfo);
                		PreparedStatement insertfirstrow = connection.prepareStatement(firstpaymentrow);
                		Statement sqlLoopUpdate = connection.createStatement();
                        PreparedStatement insertCalculationStatement = connection.prepareStatement(calculationinfo))
                         {
                		
                	
                	
                	
                	   
                       // Insert customer information
                       insertCustomerStatement.setString(1, fullName);
                       insertCustomerStatement.setInt(2, age);
                       insertCustomerStatement.setString(3, Employment);
                       insertCustomerStatement.setInt(4, monthlySalary);
                       insertCustomerStatement.executeUpdate();

                       // Insert calculation information
                       insertCalculationStatement.setDouble(1, principal);
                       insertCalculationStatement.setDouble(2, annualInterest);
                       insertCalculationStatement.setInt(3, years);
                       insertCalculationStatement.executeUpdate();
                       
                       insertfirstrow.setString(1, firstprincipal);
                       insertfirstrow.executeUpdate();


                       try (PreparedStatement insertPaymentStatement = connection.prepareStatement(paymentsfact)) {
                           // Loop through the payments array and insert each payment into the table
                           for (String saldo : payments) {
                               insertPaymentStatement.setString(1, saldo);
                               insertPaymentStatement.executeUpdate();
                           }
                       }
                       
                       sqlLoopUpdate.execute(sqlLoop);

                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
            }
        


    }


