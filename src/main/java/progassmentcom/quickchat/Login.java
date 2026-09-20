package progassmentcom.quickchat;

public class Login {
    String username;
    String password;
    String cellPhoneNumber;
    String firstName;
    String lastName;

    public Login(String username, String password, String cellPhoneNumber) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = "";
        this.lastName = "";
    }

    public boolean checkUserName() {
        // Username must contain underscore AND be 5 characters or fewer
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            // Special characters are anything that isn't alphanumeric
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasNumber && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber() {
        // Pattern: + followed by country code (1-3 digits) + local number (9-10 digits)
        String pattern = "^\\+\\d{1,3}\\d{9,10}$";
        return cellPhoneNumber != null && cellPhoneNumber.matches(pattern);
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "User successfully registered.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        // Check if entered credentials match stored credentials
        return this.username != null && this.username.equals(enteredUsername)
                && this.password != null && this.password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            // Build welcome message with first and last name if provided
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
