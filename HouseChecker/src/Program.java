import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        for (int i = 0; i < 100; i++) {
            System.out.println(" ");
        }
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter an address to be checked in the following format: Address, ZipCode");
            String user = scanner.nextLine();

            System.out.println("Address introduced: " + user);
            kotlinPart o = new kotlinPart();
            o.run(user);
            System.out.println(" ");
            System.out.println("--------------------------------------------");
            System.out.println("If you wish to exit the program, use CTRL+C");
            System.out.println("--------------------------------------------");
            System.out.println(" ");
        }
    }
}
