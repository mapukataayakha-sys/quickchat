# QuickChat

QuickChat is a Java console application that demonstrates registration, login validation, message handling, JSON persistence, and menu-driven decision-making with `if/else`, logical operators, and `switch` statements.

## Features

- Register and log in with username, password, and South African mobile number validation
- Create messages with generated IDs and hashes
- Validate recipient numbers and message length
- Send, disregard, or store messages
- Persist stored messages in JSON format
- Search stored messages by ID or recipient
- Find the longest message and display sender/recipient reports
- Demonstrate the use of JUnit 5 tests

## Technology stack

- Java 21
- Apache Maven
- JUnit 5
- Gson 2.10.1

## Prerequisites

- JDK 21 or later
- Maven 3.6 or later

## Run the application

Clone the repository:

```bash
git clone https://github.com/mapukataayakha-sys/quickchat.git
cd quickchat
```

Build the project:

```bash
mvn clean install
```

Run the console application:

```bash
mvn exec:java
```

## Run tests

```bash
mvn test
```

## Validation rules

### Username

- Must contain an underscore (`_`)
- Must not exceed five characters
- Example: `kyl_1`

### Password

- At least eight characters
- At least one uppercase letter
- At least one number
- At least one special character

### Mobile number

- Must use the international South African format
- Example: `+27838968976`

### Messages

- Maximum message length: 250 characters
- Recipient numbers must use the `+27` format

## Project structure

```text
quickchat/
├── pom.xml
├── README.md
└── src/
    ├── main/java/progassmentcom/quickchat/
    │   ├── Login.java
    │   ├── Message.java
    │   └── QuickChat.java
    └── test/java/progassmentcom/quickchat/
        ├── LoginTest.java
        ├── MessageTest.java
        └── QuickChatTest.java
```

## Main classes

### `Login`

Handles registration and login. It validates usernames, passwords, mobile numbers, and credentials.

### `Message`

Represents a message and provides recipient and length validation, message hashing, JSON storage, and message printing behavior.

### `QuickChat`

Provides the interactive console menu and application flow, including registration, login, message creation, stored-message loading, searching, and reporting.

## Example workflow

1. Register with a valid username, password, and mobile number.
2. Log in using the registered credentials.
3. Choose the number of messages to create.
4. Enter recipient details and message content.
5. Send, disregard, or store each message.
6. Search, report on, or review previously stored messages.

## JSON storage

Stored messages are serialized with Gson and read again when the application starts. The README and source are intended as a Java learning exercise; the application is not designed as a production messaging service.

## License

This project is provided for educational purposes.
