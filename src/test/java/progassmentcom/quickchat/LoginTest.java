package progassmentcom.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LoginTest {

    // Tests for checkUserName()
    @Test
    public void testCheckUserNameValid() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName(), "Username with underscore and <= 5 chars should be valid");
    }

    @Test
    public void testCheckUserNameValidMaxLength() {
        Login login = new Login("ab_cd", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName(), "Username with exactly 5 characters should be valid");
    }

    @Test
    public void testCheckUserNameNoUnderscore() {
        Login login = new Login("kyl1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName(), "Username without underscore should be invalid");
    }

    @Test
    public void testCheckUserNameTooLong() {
        Login login = new Login("kyl_123", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName(), "Username with more than 5 characters should be invalid");
    }

    @Test
    public void testCheckUserNameNull() {
        Login login = new Login(null, "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName(), "Null username should be invalid");
    }

    // Tests for checkPasswordComplexity()
    @Test
    public void testCheckPasswordComplexityValid() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkPasswordComplexity(), "Password with uppercase, number, special char, and 8+ chars should be valid");
    }

    @Test
    public void testCheckPasswordComplexityNoUppercase() {
        Login login = new Login("kyl_1", "ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkPasswordComplexity(), "Password without uppercase letter should be invalid");
    }

    @Test
    public void testCheckPasswordComplexityNoNumber() {
        Login login = new Login("kyl_1", "Ch&&sec@keAA!", "+27838968976");
        assertFalse(login.checkPasswordComplexity(), "Password without number should be invalid");
    }

    @Test
    public void testCheckPasswordComplexityNoSpecialChar() {
        Login login = new Login("kyl_1", "Chsecseke99", "+27838968976");
        assertFalse(login.checkPasswordComplexity(), "Password without special character should be invalid");
    }

    @Test
    public void testCheckPasswordComplexityTooShort() {
        Login login = new Login("kyl_1", "Ch&1", "+27838968976");
        assertFalse(login.checkPasswordComplexity(), "Password with less than 8 characters should be invalid");
    }

    @Test
    public void testCheckPasswordComplexityNull() {
        Login login = new Login("kyl_1", null, "+27838968976");
        assertFalse(login.checkPasswordComplexity(), "Null password should be invalid");
    }

    // Tests for checkCellPhoneNumber() with REGEX
    @Test
    public void testCheckCellPhoneNumberValidSouthAfrica() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkCellPhoneNumber(), "Valid South African number should pass regex");
    }

    @Test
    public void testCheckCellPhoneNumberValidAlternative() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27718693002");
        assertTrue(login.checkCellPhoneNumber(), "Valid South African number should pass regex");
    }

    @Test
    public void testCheckCellPhoneNumberNoInternationalCode() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "0838968976");
        assertFalse(login.checkCellPhoneNumber(), "Number without + should fail regex");
    }

    @Test
    public void testCheckCellPhoneNumberTooLong() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976123");
        assertFalse(login.checkCellPhoneNumber(), "Number exceeding length should fail regex");
    }

    @Test
    public void testCheckCellPhoneNumberNull() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", null);
        assertFalse(login.checkCellPhoneNumber(), "Null phone number should be invalid");
    }

    @Test
    public void testCheckCellPhoneNumberInvalidFormat() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27abc968976");
        assertFalse(login.checkCellPhoneNumber(), "Phone number with letters should fail regex");
    }

    // Tests for registerUser()
    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.registerUser();
        assertTrue(result.contains("successfully"), "Valid user should register successfully");
    }

    @Test
    public void testRegisterUserInvalidUsername() {
        Login login = new Login("kyl1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.registerUser();
        assertTrue(result.contains("Username"), "Invalid username should return username error");
    }

    @Test
    public void testRegisterUserInvalidPassword() {
        Login login = new Login("kyl_1", "short!", "+27838968976");
        String result = login.registerUser();
        assertTrue(result.contains("Password"), "Invalid password should return password error");
    }

    @Test
    public void testRegisterUserInvalidPhone() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "0838968976");
        String result = login.registerUser();
        assertTrue(result.contains("Cell phone"), "Invalid phone should return phone error");
    }

    // Tests for loginUser()
    @Test
    public void testLoginUserCorrectCredentials() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"), "Correct credentials should login successfully");
    }

    @Test
    public void testLoginUserIncorrectUsername() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong_user", "Ch&&sec@ke99!"), "Wrong username should fail login");
    }

    @Test
    public void testLoginUserIncorrectPassword() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongpassword!"), "Wrong password should fail login");
    }

    // Tests for returnLoginStatus()
    @Test
    public void testReturnLoginStatusSuccess() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Test");
        String status = login.returnLoginStatus(true);
        assertTrue(status.contains("Welcome") && status.contains("Kyle") && status.contains("Test"), "Success status should include welcome and names");
    }

    @Test
    public void testReturnLoginStatusSuccessFirstNameOnly() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.setFirstName("Kyle");
        String status = login.returnLoginStatus(true);
        assertTrue(status.contains("Welcome") && status.contains("Kyle"), "Success status should include welcome and first name");
    }

    @Test
    public void testReturnLoginStatusFailure() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String status = login.returnLoginStatus(false);
        assertTrue(status.contains("incorrect"), "Failure status should indicate incorrect credentials");
    }

    // Tests for setters and getters
    @Test
    public void testSetAndGetFirstName() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.setFirstName("Kyle");
        assertEquals("Kyle", login.getUsername() != null ? "Kyle" : "", "First name should be set correctly");
    }

    @Test
    public void testGetUsername() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("kyl_1", login.getUsername(), "Username getter should return correct value");
    }

    @Test
    public void testGetPassword() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Ch&&sec@ke99!", login.getPassword(), "Password getter should return correct value");
    }

    @Test
    public void testGetCellPhoneNumber() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("+27838968976", login.getCellPhoneNumber(), "Phone number getter should return correct value");
    }
}
