# QuickChat - Messaging Application

## Project Overview

QuickChat is a Java-based messaging application that implements user registration, authentication, and message management with JSON-based storage. This project is divided into three parts, with comprehensive testing and professional development practices.

## Features

### Part 1: User Registration & Login
- **Username Validation**: Must contain underscore and be ≤ 5 characters
- **Password Complexity**: Requires uppercase letter, number, special character, and ≥ 8 characters
- **Phone Validation**: Uses regex pattern for international phone numbers (e.g., +27838968976)
- **Secure Login**: Validates credentials against registered user data

### Part 2: Message Management
- **Message Creation**: Generate unique 10-digit message IDs for each message
- **Message Validation**: 
  - Recipient validation with international phone number format
  - Message length limit of 250 characters
- **Message Hash**: Format `firstTwoDigits:messageNumber:firstWordLastWord`
- **Message Status**: Track sent, received, and read flags
- **Multiple Actions**: Send, store, or discard messages

### Part 3: Storage & Reporting
- **JSON Serialization**: Persist messages to JSON files using Google's Gson library
- **Message Retrieval**: Load stored messages from JSON on application startup
- **Find Longest Message**: Display the longest message by character count
- **Search by Message ID**: Find specific messages and display recipient + content
- **Sender & Recipient Report**: Display sender information alongside recipient details
- **Search by Recipient**: Filter messages by phone number across all lists

## Technology Stack

- **Language**: Java 21
- **Build Tool**: Maven
- **Testing**: JUnit 5 (Jupiter)
- **JSON Library**: Google Gson 2.10.1

## Project Structure

```
quickchatprog/
├── pom.xml                                    # Maven configuration
├── src/
│   ├── main/java/progassmentcom/quickchat/
│   │   ├── Login.java                        # User registration & authentication
│   │   ├── Message.java                      # Message creation & validation
│   │   └── QuickChat.java                    # Main application & menu
│   └── test/java/progassmentcom/quickchat/
│       ├── LoginTest.java                    # Login class unit tests (25+ tests)
│       ├── MessageTest.java                  # Message class unit tests (30+ tests)
│       └── QuickChatTest.java               # QuickChat class unit tests (20+ tests)
└── README.md                                 # This file
```

## Key Classes

### Login.java
Handles user registration and authentication with validation:
- `checkUserName()`: Validates username format with regex
- `checkPasswordComplexity()`: Ensures password meets complexity requirements
- `checkCellPhoneNumber()`: Validates phone number using regex pattern `^\\+\\d{1,3}\\d{9,10}$`
- `registerUser()`: Complete registration validation
- `loginUser()`: Authenticate with username and password

### Message.java
Manages individual messages with JSON serialization:
- `checkMessageID()`: Verifies 10-digit message ID
- `checkRecipientCell()`: Validates recipient phone number
- `createMessageHash()`: Generates message hash
- `checkMessageLength()`: Validates message ≤ 250 characters
- `storeToJSON()`: Persists message to JSON file (using Gson)
- Status flags: `messageSent`, `messageReceived`, `messageRead`

### QuickChat.java
Main application with menu-driven interface:
- `sendMessages()`: Interactive message creation loop
- `findLongestMessage()`: Find longest message by content length
- `searchByMessageID()`: Search for specific message by ID
- `searchByRecipient()`: Filter messages by phone number
- `displaySenderAndRecipient()`: Generate sender/recipient report
- `readStoredMessagesFromJSON()`: Load messages from JSON file on startup
- `deleteByHash()`: Delete messages with confirmation

## Usage

### Building the Project
```bash
mvn clean install
```

### Running the Application
```bash
mvn exec:java
```

### Running Unit Tests
```bash
mvn test
```

### Running Specific Test Class
```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=MessageTest
mvn test -Dtest=QuickChatTest
```

## Example Workflow

1. **Registration**
   ```
   Username: kyl_1
   Password: Ch&&sec@ke99!
   Phone: +27838968976
   ```

2. **Send Messages**
   - Enter number of messages
   - Input recipient and content for each
   - Choose action: Send (1), Discard (2), Store (3)

3. **View Stored Messages**
   - Messages stored with choice 3 are saved to `stored_messages.json`
   - Automatically loaded on next application run

4. **Search & Report**
   - Find longest message
   - Search by message ID
   - View sender/recipient report
   - Search by recipient phone number

## Data Formats

### Message JSON Format
```json
{
  "messageID": "1234567890",
  "messageNumber": 1,
  "recipient": "+27838968976",
  "messageContent": "Sample message",
  "messageHash": "12:1:SAMPLEMESSAGE",
  "messageSent": true,
  "messageReceived": false,
  "messageRead": false
}
```

### Message Hash Format
```
FirstTwoDigitsOfID:MessageNumber:FirstWordLastWord
Example: 12:1:SAMPLEMESSAGE
```

## Validation Rules

### Username
- ✓ Must contain underscore (`_`)
- ✓ Must be ≤ 5 characters
- ✓ Example: `kyl_1`, `ab_cd`

### Password
- ✓ Must be ≥ 8 characters
- ✓ Must contain uppercase letter (A-Z)
- ✓ Must contain digit (0-9)
- ✓ Must contain special character (!@#$%^&*)
- ✓ Example: `Ch&&sec@ke99!`

### Phone Number (Regex: `^\\+\\d{1,3}\\d{9,10}$`)
- ✓ Must start with `+`
- ✓ Country code 1-3 digits
- ✓ Local number 9-10 digits
- ✓ Total length ≤ 13 characters
- ✓ Example: `+27838968976` (South Africa)

### Message Content
- ✓ Maximum 250 characters
- ✓ Required (not empty)

## Testing

### Test Coverage
- **75+ comprehensive unit tests** across three test classes
- Tests for all validation methods
- Integration tests for complete workflows
- Edge case handling (null values, boundary conditions)

### Test Categories

**LoginTest.java** (25+ tests)
- Username validation (valid, invalid, boundary cases)
- Password complexity (all requirements tested)
- Phone number regex validation
- Registration process validation
- Login authentication
- Welcome message generation

**MessageTest.java** (30+ tests)
- Message ID generation and validation
- Recipient validation
- Message hash creation and format
- Message length validation
- Message status flag management
- Complete message workflow

**QuickChatTest.java** (20+ tests)
- Find longest message
- Search by message ID
- Search by recipient (single/multiple matches)
- Sender/recipient report generation
- JSON file reading (file not found, empty list)
- Multi-message workflows
- Message status flags in context

## Git Commit History

This project follows professional commit practices with feature branches:

**Feature Branches Created:**
- `feature/part1-registration-login` - Part 1 implementation
- `feature/part2-messaging` - Part 2 implementation
- `feature/part3-storage-reporting` - Part 3 implementation

**Commit Types:**
- `feat:` - New features
- `fix:` - Bug fixes
- `test:` - Test additions
- `refactor:` - Code improvements

## Dependencies

### Production Dependencies
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

### Test Dependencies
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.10.3</version>
    <scope>test</scope>
</dependency>
```

## Key Implementation Details

### Regex Patterns Used

**Phone Number Validation:**
```java
String pattern = "^\\+\\d{1,3}\\d{9,10}$";
cellPhoneNumber.matches(pattern)
```

### JSON Storage Implementation

**Serialization (Writing to File):**
```java
Gson gson = new GsonBuilder().setPrettyPrinting().create();
String json = gson.toJson(message);
FileWriter writer = new FileWriter(filename, true);
writer.write(json + "\n");
```

**Deserialization (Reading from File):**
```java
Gson gson = new Gson();
String[] jsonLines = content.trim().split("\n");
for (String line : jsonLines) {
    Message msg = gson.fromJson(line, Message.class);
    messages.add(msg);
}
```

## Error Handling

- Graceful handling of missing JSON files
- Null value validation throughout
- User-friendly error messages for validation failures
- Exception handling for file I/O operations
- Try-catch blocks for JSON parsing

## Future Enhancements

- Database integration (replacing JSON files)
- User profile pictures
- Message encryption
- Typing indicators
- Read receipts with timestamps
- Message groups/conversations
- User presence status
- Message search with filters

## Author

Developed by: mapukataayakha-sys

## License

This project is part of a programming assessment and is provided as-is.

## Version History

### v1.0.0 (Current)
- ✓ Part 1: User Registration & Login with regex validation
- ✓ Part 2: Message Management with status flags
- ✓ Part 3: JSON Storage & Advanced Reporting
- ✓ Comprehensive unit testing (75+ tests)
- ✓ Professional documentation

## Submission Details

**Completion Date:** 2026-09-12
**Total Commits:** 10+ across feature branches
**Total Unit Tests:** 75+
**Code Coverage:** All classes and methods tested

For questions or issues, please refer to the project repository or documentation in the code.
