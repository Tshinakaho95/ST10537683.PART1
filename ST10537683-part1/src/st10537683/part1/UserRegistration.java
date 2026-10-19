
package st10537683.part1;

import java.util.Scanner;

// Main class: handles user registration, login and QuickChat
public class UserRegistration {

    private static String userName;
    private static String password;
    private static String cellPhoneNumber;
    private static String firstName;
    private static String lastName;

    public static void setFirstName(String fname) {
        firstName = fname;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setUserName(String username) {
        userName = username;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setPassword(String pass) {
        password = pass;
    }

    public static String getPassword() {
        return password;
    }

    public static void setLastname(String lname) {
        lastName = lname;
    }

    public static String getLastname() {
        return lastName;
    }

    public static void setCellNumber(String cellphone) {
        cellPhoneNumber = cellphone;
    }

    public static String getCellNumber() {
        return cellPhoneNumber;
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("***** User Registration *****");

        // Enter first name
        System.out.print("Please enter your first name: ");
        firstName = input.nextLine();

        if (firstName.trim().isEmpty()) {
            System.out.println("First name cannot be empty.");
            input.close();
            return;
        }

        // Enter surname
        System.out.print("Please enter your last name: ");
        lastName = input.nextLine();

        if (lastName.trim().isEmpty()) {
            System.out.println("Last name cannot be empty.");
            input.close();
            return;
        }

        // Create Login object
        Login login = new Login(firstName, lastName);

        // Register username
        while (true) {
            System.out.print(
                "Enter your username (maximum 5 characters "
                + "and must contain an underscore): "
            );

            userName = input.nextLine();

            if (login.checkUserName(userName)) {
                break;
            }

            System.out.println(
                "Username is incorrectly formatted. "
                + "It must contain an underscore and be "
                + "no more than five characters long."
            );
        }

        // Register password
        while (true) {
            System.out.print(
                "Create a password (at least 8 characters, "
                + "one capital letter, one number and "
                + "one special character): "
            );

            password = input.nextLine();

            if (login.checkPasswordComplexity(password)) {
                break;
            }

            System.out.println(
                "Password is incorrectly formatted. "
                + "Please try again."
            );
        }

        // Register cellphone number
        while (true) {
            System.out.print(
                "Enter cellphone number with international "
                + "country code: "
            );

            cellPhoneNumber = input.nextLine();

            if (login.checkCellPhoneNumber(cellPhoneNumber)) {
                break;
            }

            System.out.println(
                "Cellphone number is incorrectly formatted "
                + "or does not contain the international code."
            );
        }

        // Register user
        String regStatus = login.registerUser(
            userName,
            password,
            cellPhoneNumber,
            firstName,
            lastName
        );

        System.out.println(regStatus);

        // Continue to login only if registration succeeds
        if (!regStatus.equals("Registration successful.")) {
            input.close();
            return;
        }

        System.out.println("\n****** User Login ******");

        System.out.print("Enter the username you created: ");
        String loginUser = input.nextLine();

        System.out.print("Enter the password you created: ");
        String loginPass = input.nextLine();

        // Check login details
        String loginStatus = login.returnLoginStatus(
            loginUser,
            loginPass
        );

        System.out.println(loginStatus);

        // LINK: Start Messages only after successful login
        if (loginStatus.toLowerCase().contains("welcome")) {
            System.out.println("\nLogin successful!");
            System.out.println("Opening QuickChat...\n");

            Messages.startQuickChat();
        } else {
            System.out.println(
                "Login failed. QuickChat cannot be opened."
            );
        }

        input.close();
    }
}
