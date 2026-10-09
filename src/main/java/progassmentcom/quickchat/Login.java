package progassmentcom.quickchat;

public class Login {
    private static final int MAX_USERNAME_LENGTH = 5;
    private static final String USERNAME_PATTERN = "^(?=.*_)[A-Za-z0-9_]{1,5}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
    private static final String PHONE_PATTERN = "^\\+27\\d{9}$";

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean lastLoginSuccess;

    public Login(String username, String password, String cellPhoneNumber) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = "";
        this.lastName = "";
        this.lastLoginSuccess = false;
    }

    public boolean checkUserName() {
        return username != null && username.matches(USERNAME_PATTERN);
    }

    public boolean checkPasswordComplexity() {
        return password != null && password.matches(PASSWORD_PATTERN);
    }

    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null && cellPhoneNumber.matches(PHONE_PATTERN);
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        return "User successfully registered.";
    }

    public String registerUser(String username, String password, String cellPhoneNumber) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        return registerUser();
    }

    public boolean loginUser() {
        lastLoginSuccess = this.username != null && this.password != null;
        return lastLoginSuccess;
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }

        lastLoginSuccess = this.username != null && this.username.equals(enteredUsername)
                && this.password != null && this.password.equals(enteredPassword);
        return lastLoginSuccess;
    }

    public String returnLoginStatus() {
        return returnLoginStatus(lastLoginSuccess);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            String displayFirst = (firstName == null || firstName.isBlank()) ? "User" : firstName;
            String displayLast = (lastName == null || lastName.isBlank()) ? "" : lastName;

            if (displayLast.isEmpty()) {
                return "Welcome " + displayFirst + ", it is great to see you again.";
            }
            return "Welcome " + displayFirst + ", " + displayLast + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
