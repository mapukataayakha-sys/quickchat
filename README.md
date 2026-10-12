# QuickChat

QuickChat is a Java console application that demonstrates user registration, secure login validation, and message management. It uses conditional logic (`if`/`else`, `switch`, and boolean checks) to validate input and process user actions such as sending, storing, and discarding messages.

## Project overview

This project was built as a Java-based learning exercise to practice:

- Input validation and form checks
- Decision-making logic using conditionals
- File persistence with JSON
- Menu-driven application flow
- Unit testing with JUnit

The application allows a user to register with a username, password, and mobile number, log in, and then create messages for a recipient. Messages can be sent, stored, or disregarded, and stored messages can be reloaded from a JSON file.

## Features

### User registration and login
- Validates a username using the required format
- Checks password complexity requirements
- Validates a South African mobile number format
- Allows login using the registered credentials
- Displays a welcome message after successful login

### Message handling
- Creates a message with a generated ID
- Validates the recipient phone number
- Checks whether the message length is within the allowed limit
- Generates a message hash using a defined format
- Supports actions:
  - Send
  - Disregard
  - Store

### Storage and reporting
- Saves stored messages to a JSON file
- Reads previously stored messages when the app starts
- Finds the longest message
- Searches messages by ID
- Searches messages by recipient
- Displays sender/recipient report information

## Technology stack

- Java 21
- Maven
- JUnit 5
- Gson for JSON serialization

## Project structure

```text
quickchat/
├── pom.xml
├── README.md
├── src/
│   ├── main/java/progassmentcom/quickchat/
│   │   ├── Login.java
│   │   ├── Message.java
│   │   └── QuickChat.java
│   └── test/java/progassmentcom/quickchat/
│       ├── LoginTest.java
│       ├── MessageTest.java
│       └── QuickChatTest.java
└── target/
```

## Main classes

### Login.java
Handles registration and login validation.

Key responsibilities:
- `checkUserName()`
- `checkPasswordComplexity()`
- `checkCellPhoneNumber()`
- `registerUser()`
- `loginUser()`
- `returnLoginStatus()`

### Message.java
Represents a single message and includes validation and storage logic.

Key responsibilities:
- `checkRecipientCell()`
- `checkMessageLength()`
- `createMessageHash()`
- `storeToJSON()`
- `printMessage()`
- `sentMessage()`

### QuickChat.java
Provides the menu-driven console application that ties the system together.

Key responsibilities:
- user registration flow
- login flow
- sending messages
- reading stored JSON messages
- searching and reporting

## Validation rules

### Username
- Must contain an underscore (`_`)
- Must not exceed 5 characters
- Example: `kyl_1`

### Password
- At least 8 characters long
- Contains at least one uppercase letter
- Contains at least one number
- Contains at least one special character

### Phone number
- Must be in the format `+27...`
- Example: `+27838968976`

### Message content
- Maximum length: 250 characters

## How to run

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
mvn exec:java
```

### Run tests

```bash
mvn test
```

## Example workflow

1. Register a user with a valid username, password, and phone number.
2. Log in with the same credentials.
3. Enter the number of messages to create.
4. Add recipient details and message text.
5. Choose whether to send, disregard, or store each message.
6. View stored messages from the JSON file during later runs.

## Notes

This project is intended as an educational Java exercise and focuses on practical application of control flow, validation logic, and JSON-based persistence. It is not a full production messaging platform, but it demonstrates core programming concepts clearly and effectively.

## License

This project is provided for educational and learning purposes.
