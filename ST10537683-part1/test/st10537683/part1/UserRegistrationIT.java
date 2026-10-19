
package st10537683.part1;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.*;

public class UserRegistrationIT {

    private Login login;

    // Variables used to calculate the test results
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    // Count tests as they run
    @Rule
    public TestWatcher testWatcher = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            totalTests++;
        }

        @Override
        protected void succeeded(Description description) {
            passedTests++;
        }

        @Override
        protected void failed(Throwable e, Description description) {
            failedTests++;
            System.out.println(
                    "FAILED TEST: " + description.getMethodName()
            );
            System.out.println("Reason: " + e.getMessage());
        }
    };

    @Before
    public void setUp() {
        login = new Login("John", "Smith");
    }

    // ---------------------------------------------------------
    // USERNAME TESTS
    // ---------------------------------------------------------

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameWithoutUnderscore() {
        assertFalse(login.checkUserName("kyle1"));
    }

    @Test
    public void testUsernameLongerThanFiveCharacters() {
        assertFalse(login.checkUserName("kyl_123"));
    }

    @Test
    public void testUsernameWithUnderscoreAndFiveCharacters() {
        assertTrue(login.checkUserName("ab_cd"));
    }

    // ---------------------------------------------------------
    // PASSWORD TESTS
    // ---------------------------------------------------------

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        assertTrue(
                login.checkPasswordComplexity("Ch&&sec@ke99!")
        );
    }

    @Test
    public void testPasswordTooShort() {
        assertFalse(login.checkPasswordComplexity("Pa1!"));
    }

    @Test
    public void testPasswordWithoutCapitalLetter() {
        assertFalse(
                login.checkPasswordComplexity("password1!")
        );
    }

    @Test
    public void testPasswordWithoutNumber() {
        assertFalse(
                login.checkPasswordComplexity("Password!")
        );
    }

    @Test
    public void testPasswordWithoutSpecialCharacter() {
        assertFalse(
                login.checkPasswordComplexity("Password1")
        );
    }

    // ---------------------------------------------------------
    // CELLPHONE NUMBER TESTS
    // ---------------------------------------------------------

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        assertTrue(
                login.checkCellPhoneNumber("+27838968976")
        );
    }

    @Test
    public void testCellPhoneNumberWithoutInternationalCode() {
        assertFalse(
                login.checkCellPhoneNumber("0838968976")
        );
    }

    @Test
    public void testCellPhoneNumberContainsLetters() {
        assertFalse(
                login.checkCellPhoneNumber("+27838968ABC")
        );
    }

    @Test
    public void testCellPhoneNumberTooShort() {
        assertFalse(
                login.checkCellPhoneNumber("+278389689")
        );
    }

    // ---------------------------------------------------------
    // REGISTRATION TESTS
    // ---------------------------------------------------------

    @Test
    public void testSuccessfulRegistration() {
        String result = login.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals("Registration successful.", result);
    }

    @Test
    public void testRegistrationWithInvalidUsername() {
        String result = login.registerUser(
                "kyle1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Username is not correctly formatted, please ensure that "
                + "your username contains an underscore and is no more "
                + "than five characters in length.",
                result
        );
    }

    @Test
    public void testRegistrationWithInvalidPassword() {
        String result = login.registerUser(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, a "
                + "capital letter, a number, and a special character.",
                result
        );
    }

    @Test
    public void testRegistrationWithInvalidCellPhoneNumber() {
        String result = login.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "0838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Cell phone number incorrectly formatted or does not "
                + "contain international code.",
                result
        );
    }

    // ---------------------------------------------------------
    // LOGIN TESTS
    // ---------------------------------------------------------

    @Test
    public void testLoginSuccessful() {
        registerValidUser();

        assertTrue(
                login.loginUser("kyl_1", "Ch&&sec@ke99!")
        );
    }

    @Test
    public void testLoginFailedWithIncorrectUsername() {
        registerValidUser();

        assertFalse(
                login.loginUser("wrong_user", "Ch&&sec@ke99!")
        );
    }

    @Test
    public void testLoginFailedWithIncorrectPassword() {
        registerValidUser();

        assertFalse(
                login.loginUser("kyl_1", "WrongPassword1!")
        );
    }

    @Test
    public void testSuccessfulLoginStatusMessage() {
        registerValidUser();

        String result = login.returnLoginStatus(
                "kyl_1",
                "Ch&&sec@ke99!"
        );

        assertEquals(
                "Welcome Kyle Smith, it is great to see you again.",
                result
        );
    }

    @Test
    public void testFailedLoginStatusMessage() {
        registerValidUser();

        String result = login.returnLoginStatus(
                "kyl_1",
                "WrongPassword1!"
        );

        assertEquals(
                "Username or password incorrect, please try again.",
                result
        );
    }

    // ---------------------------------------------------------
    // USERREGISTRATION GETTER AND SETTER TESTS
    // ---------------------------------------------------------

    @Test
    public void testFirstNameGetterAndSetter() {
        UserRegistration.setFirstName("Kyle");

        assertEquals(
                "Kyle",
                UserRegistration.getFirstName()
        );
    }

    @Test
    public void testLastNameGetterAndSetter() {
        UserRegistration.setLastname("Smith");

        assertEquals(
                "Smith",
                UserRegistration.getLastname()
        );
    }

    @Test
    public void testUsernameGetterAndSetter() {
        UserRegistration.setUserName("kyl_1");

        assertEquals(
                "kyl_1",
                UserRegistration.getUserName()
        );
    }

    @Test
    public void testPasswordGetterAndSetter() {
        UserRegistration.setPassword("Ch&&sec@ke99!");

        assertEquals(
                "Ch&&sec@ke99!",
                UserRegistration.getPassword()
        );
    }

    @Test
    public void testCellNumberGetterAndSetter() {
        UserRegistration.setCellNumber("+27838968976");

        assertEquals(
                "+27838968976",
                UserRegistration.getCellNumber()
        );
    }

    // ---------------------------------------------------------
    // HELPER METHOD
    // ---------------------------------------------------------

    private void registerValidUser() {
        login.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );
    }

    // ---------------------------------------------------------
    // FINAL JUNIT TEST REPORT
    // ---------------------------------------------------------

    @AfterClass
    public static void showTestPercentage() {

        double percentage = totalTests == 0
                ? 0.0
                : (passedTests * 100.0) / totalTests;

        System.out.println();
        System.out.println("========================================");
        System.out.println("        JUNIT 4 TEST REPORT");
        System.out.println("========================================");
        System.out.println("Total tests:  " + totalTests);
        System.out.println("Passed tests: " + passedTests);
        System.out.println("Failed tests: " + failedTests);

        System.out.printf(
                "Pass percentage: %.2f%%%n",
                percentage
        );

        System.out.println("========================================");
    }
}   