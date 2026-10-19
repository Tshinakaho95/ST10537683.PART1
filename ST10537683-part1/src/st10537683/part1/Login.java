
package st10537683.part1;

public class Login {

    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Variables for checking login
    private String storedUsername;
    private String storedPassword;

    public Login(String fname, String lname) {
        this.firstName = fname;
        this.lastName = lname;
    }

    // Check username format
    public boolean checkUserName(String username) {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    // Check password complexity
    public boolean checkPasswordComplexity(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasNumber = false;
        boolean hasCap = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char current = password.charAt(i);

            if (Character.isUpperCase(current)) {
                hasCap = true;
            }

            if (Character.isDigit(current)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(current)) {
                hasSpecialChar = true;
            }
        }

        return hasNumber && hasCap && hasSpecialChar;
    }

    // Check international cellphone number
    public boolean checkCellPhoneNumber(String number) {

        if (number == null) {
            return false;
        }

        String regex = "^\\+\\d{11,13}$";
        return number.matches(regex);
    }

    // Register user
    public String registerUser(
            String username,
            String password,
            String cellNumber,
            String fName,
            String lName) {

        this.firstName = fName;
        this.lastName = lName;

        if (!checkUserName(username)) {
            return "Username is not correctly formatted, "
                    + "please ensure that your username contains "
                    + "an underscore and is no more than five "
                    + "characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; "
                    + "please ensure that the password contains "
                    + "at least eight characters, a capital letter, "
                    + "a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number incorrectly formatted "
                    + "or does not contain international code.";
        }

        // Store registration details for login
        this.cellPhoneNumber = cellNumber;
        this.storedUsername = username;
        this.storedPassword = password;

        return "Registration successful.";
    }

    // Check login credentials
    public boolean loginUser(String username, String password) {

        return username != null
                && password != null
                && username.equals(this.storedUsername)
                && password.equals(this.storedPassword);
    }

    // Return login status
    public String returnLoginStatus(
            String username,
            String password) {

        if (loginUser(username, password)) {
            return "Welcome " + this.firstName + " "
                    + this.lastName
                    + ", it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }

    // LINK LOGIN TO MESSAGES
    public void openQuickChat(
            String username,
            String password) {

        if (loginUser(username, password)) {

            System.out.println(returnLoginStatus(
                    username, password));

            System.out.println("\nOpening QuickChat...\n");

            // Call the Messages class
            Messages.startQuickChat();

        } else {

            System.out.println(
                    "Login failed. QuickChat cannot be opened.");
        }
    }
}
