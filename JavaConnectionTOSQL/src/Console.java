import java.util.Scanner;

public class Console {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static double readNumber(String prompt){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }
    public static double readNumber(String prompt, double min, double max) {
        double value;
        while (true) {
            System.out.print(prompt);
            value = SCANNER.nextDouble();
            if (value >= min && value <= max)
                return value;
            System.out.println("Podaj wartość pomiędzy " + min + " a " + max);
        }
    }
    public static String getName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj imię i nazwisko: ");
        return scanner.nextLine();
    }
    
    public static String getEmployment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj formę zatrudnienia: ");
        return scanner.nextLine();
    }

    public static Integer getAge() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj swój wiek: ");
        return scanner.nextInt();
    }

    public static Integer getMonthlySalary() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj miesięczne zarobki: ");
        return scanner.nextInt();
    }
}
