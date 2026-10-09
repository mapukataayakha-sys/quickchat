package progassmentcom.quickchat;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class QuickChat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to QuickChat.");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter cell phone number: ");
        String phoneNumber = scanner.nextLine();

        Login loginSystem = new Login(username, password, phoneNumber);
        String registrationResult = loginSystem.registerUser();
        System.out.println(registrationResult);

        if (registrationResult.contains("successfully")) {
            System.out.print("Enter username to login: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password to login: ");
            String loginPassword = scanner.nextLine();

            if (loginSystem.loginUser(loginUsername, loginPassword)) {
                System.out.print("Enter first name: ");
                loginSystem.setFirstName(scanner.nextLine());
                System.out.print("Enter last name: ");
                loginSystem.setLastName(scanner.nextLine());
                System.out.println(loginSystem.returnLoginStatus(true));
                runApplication(scanner, loginUsername);
            } else {
                System.out.println(loginSystem.returnLoginStatus(false));
            }
        }

        scanner.close();
    }

    private static void runApplication(Scanner scanner, String loggedInUsername) {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();

        boolean appRunning = true;

        while (appRunning) {
            System.out.println("Welcome to QuickChat.");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Select an option (1-3): ");

            int menuChoice = readInt(scanner);
            switch (menuChoice) {
                case 1 -> sendMessages(scanner, sentMessages, storedMessages, disregardedMessages);
                case 2 -> {
                    System.out.println("Coming Soon.");
                }
                case 3 -> {
                    System.out.println("Total messages sent: " + sentMessages.size());
                    appRunning = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void sendMessages(Scanner scanner, ArrayList<Message> sentMessages,
                                    ArrayList<Message> storedMessages, ArrayList<Message> disregardedMessages) {
        System.out.print("How many messages would you like to enter this session? ");
        int numMessages = readInt(scanner);
        if (numMessages <= 0) {
            System.out.println("Please enter a positive number of messages.");
            return;
        }

        for (int i = 1; i <= numMessages; i++) {
            System.out.println("Message " + i);
            System.out.print("Enter recipient cell number: ");
            String recipient = scanner.nextLine();
            System.out.print("Enter message (max 250 characters): ");
            String messageText = scanner.nextLine();

            Message message = new Message(recipient, messageText);
            message.setMessageNumber(i);
            System.out.println(message.checkRecipientCell());
            System.out.println(message.checkMessageLength());

            if (message.checkRecipientCell().contains("successfully") && message.checkMessageLength().contains("ready")) {
                message.createMessageHash();
                System.out.println("1) Send");
                System.out.println("2) Disregard");
                System.out.println("3) Store");
                System.out.print("Choose an option: ");

                int choice = readInt(scanner);
                String result = message.sentMessage(choice);
                System.out.println(result);

                switch (choice) {
                    case 1 -> {
                        sentMessages.add(message);
                        message.printMessage();
                    }
                    case 2 -> {
                        disregardedMessages.add(message);
                        System.out.println("Press 0 to delete the message.");
                    }
                    case 3 -> {
                        storedMessages.add(message);
                        message.storeToJSON("stored_messages.json");
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    public static ArrayList<Message> readStoredMessagesFromJSON(String filename) {
        ArrayList<Message> messages = new ArrayList<>();
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            return messages;
        }

        try {
            String content = Files.readString(path);
            String[] jsonLines = content.trim().split("\\R");
            Gson gson = new Gson();

            for (String line : jsonLines) {
                if (!line.isBlank()) {
                    Message msg = gson.fromJson(line, Message.class);
                    if (msg != null) {
                        messages.add(msg);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No stored messages file found or error reading: " + e.getMessage());
        }

        return messages;
    }

    public static String findLongestMessage(ArrayList<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return "No stored messages.";
        }

        Message longest = messages.get(0);
        for (Message message : messages) {
            if (message.getMessageContent() != null && message.getMessageContent().length() > longest.getMessageContent().length()) {
                longest = message;
            }
        }
        return longest.getMessageContent();
    }

    public static void searchByMessageID(ArrayList<Message> sentMessages,
                                        ArrayList<Message> storedMessages,
                                        String messageID) {
        for (Message message : sentMessages) {
            if (messageID != null && message.getMessageID().equals(messageID)) {
                System.out.println("Recipient: " + message.getRecipient());
                System.out.println("Message: " + message.getMessageContent());
                return;
            }
        }

        for (Message message : storedMessages) {
            if (messageID != null && message.getMessageID().equals(messageID)) {
                System.out.println("Recipient: " + message.getRecipient());
                System.out.println("Message: " + message.getMessageContent());
                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    public static void displaySenderAndRecipient(ArrayList<Message> messages, String senderUsername) {
        System.out.println(" Sender and Recipient Report ");
        for (Message message : messages) {
            System.out.println("Sender: " + senderUsername);
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("---");
        }
    }

    public static ArrayList<Message> searchByRecipient(ArrayList<Message> sentMessages,
                                                      ArrayList<Message> storedMessages,
                                                      ArrayList<Message> disregardedMessages,
                                                      String recipient) {
        ArrayList<Message> matches = new ArrayList<>();
        for (Message message : sentMessages) {
            if (recipient != null && recipient.equals(message.getRecipient())) {
                matches.add(message);
            }
        }
        for (Message message : storedMessages) {
            if (recipient != null && recipient.equals(message.getRecipient())) {
                matches.add(message);
            }
        }
        for (Message message : disregardedMessages) {
            if (recipient != null && recipient.equals(message.getRecipient())) {
                matches.add(message);
            }
        }
        return matches;
    }

    public static boolean deleteByHash(ArrayList<Message> sentMessages,
                                       ArrayList<Message> storedMessages,
                                       String hash,
                                       Scanner scanner) {
        for (int i = 0; i < sentMessages.size(); i++) {
            if (hash != null && hash.equals(sentMessages.get(i).getMessageHash())) {
                System.out.print("Are you sure you want to delete this message? (yes/no): ");
                String confirmation = scanner.nextLine().toLowerCase();
                if (confirmation.equals("yes")) {
                    sentMessages.remove(i);
                    return true;
                }
                System.out.println("Deletion cancelled.");
                return false;
            }
        }

        for (int i = 0; i < storedMessages.size(); i++) {
            if (hash != null && hash.equals(storedMessages.get(i).getMessageHash())) {
                System.out.print("Are you sure you want to delete this message? (yes/no): ");
                String confirmation = scanner.nextLine().toLowerCase();
                if (confirmation.equals("yes")) {
                    storedMessages.remove(i);
                    return true;
                }
                System.out.println("Deletion cancelled.");
                return false;
            }
        }

        return false;
    }

    public static void displayReport(ArrayList<Message> messages) {
        System.out.println(" Message Report ");
        for (Message message : messages) {
            System.out.println("Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessageContent());
        }
    }
}