import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        //Just send a bunch of empty lines to clear the user's terminal.
        for (int i = 0; i < 100; i++) {
            System.out.println(" ");
        }
        //Create an infinite loop so that the user can check as many addresses without having to run the program multiple times.
        while(true) {
            //Take user input
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter an address to be checked in the following format: Address, ZipCode");
            //Scan the input
            String user = scanner.nextLine();
            //Say the input back to the user for good measure.
            System.out.println("Address introduced: " + user);
            //Call kotlin from java.
            kotlinPart o = new kotlinPart();
            //Parse the data to kotlin
            o.run(user);
            //Let the user know that they can cancel the program whenever they want.
            System.out.println(" ");
            System.out.println("--------------------------------------------");
            System.out.println("If you wish to exit the program, use CTRL+C");
            System.out.println("--------------------------------------------");
            System.out.println(" ");
        }
    }
}
