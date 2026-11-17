package progassmentcom.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private static final String GOOD_USER = "kyl_1";
    private static final String GOOD_PASS = "Cheesec@ke99!";
    private static final String GOOD_PHONE = "+27838968976";

    // checkUserName()
    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login("kyle!!!!!!!", GOOD_PASS, GOOD_PHONE);
        assertFalse(login.checkUserName());
    }

    @Test
    public void testUsernameTooLong() {
        Login login = new Login("ky_le1", GOOD_PASS, GOOD_PHONE);
        assertFalse(login.checkUserName());
    }

    // checkPasswordComplexity()
    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login(GOOD_USER, "password", GOOD_PHONE);
        assertFalse(login.checkPasswordComplexity());
    }

    // checkCellPhoneNumber()
    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login(GOOD_USER, GOOD_PASS, "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }

    // registerUser()
    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertEquals("User successfully registered.", login.registerUser());
    }

    @Test
    public void testRegisterUserBadUsernameMessage() {
        Login login = new Login("kyle!!!!!!!", GOOD_PASS, GOOD_PHONE);
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.",
                login.registerUser());
    }

    @Test
    public void testRegisterUserBadPasswordMessage() {
        Login login = new Login(GOOD_USER, "password", GOOD_PHONE);
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser());
    }

    @Test
    public void testRegisterUserBadPhoneMessage() {
        Login login = new Login(GOOD_USER, GOOD_PASS, "08966553");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                login.registerUser());
    }

    // loginUser() and returnLoginStatus()
    @Test
    public void testLoginSuccessful() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertTrue(login.loginUser(GOOD_USER, GOOD_PASS));
    }

    @Test
    public void testLoginFailedWrongPassword() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertFalse(login.loginUser(GOOD_USER, "wrongPass1!"));
    }

    @Test
    public void testLoginStatusSuccessIncludesFirstAndLastName() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(true));
    }

    @Test
    public void testLoginStatusFailure() {
        Login login = new Login(GOOD_USER, GOOD_PASS, GOOD_PHONE);
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }
}
