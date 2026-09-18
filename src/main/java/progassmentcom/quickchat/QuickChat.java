package progassmentcom.quickchat;

import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class QuickChat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Welcome to QuickChat ");

        System.out.println("Please register to continue:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter cell phone number (e.g., +27838968976): ");
        String phoneNumber = scanner.nextLine();

        Login loginSystem = new Login(username, password, phoneNumber);
        String registrationResult = loginSystem.registerUser();
        System.out.println(registrationResult);

        // Only proceed to login if registration was successful
        if (registrationResult.contains("successfully")) {
            System.out.println("Now please login:");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            if (loginSystem.loginUser(loginUsername, loginPassword)) {
                System.out.println(loginSystem.returnLoginStatus(true));
                runApplication(scanner, loginUsername);
            } else {
                System.out.println(loginSystem.returnLoginStatus(false));
            }
        }

        scanner.close();
    }

    private static void runApplication(Scanner scanner, String loggedInUsername) {
        // Maintain separate lists for different message states
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();
        
        // Load stored messages from JSON file on startup
        storedMessages = readStoredMessagesFromJSON("stored_messages.json");

        boolean appRunning = true;

        while (appRunning) {
            System.out.println(" Welcome to QuickChat ");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) View stored messages");
            System.out.println("4) Search by message ID");
            System.out.println("5) Search by recipient");
            System.out.println("6) Delete by hash");
            System.out.println("7) Find longest message");
            System.out.println("8) View sender and recipient report");
            System.out.println("9) View report");
            System.out.println("10) Quit");
            System.out.print("Select an option (1-10): ");

            int menuChoice = readInt(scanner);

            switch (menuChoice) {
                case 1 -> sendMessages(scanner, sentMessages, storedMessages, disregardedMessages);
                case 2 -> {
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages have been sent yet.");
                    } else {
                        System.out.println("Recently sent messages:");
                        displayReport(sentMessages);
                    }
                }
                case 3 -> {
                    if (storedMessages.isEmpty()) {
                        System.out.println("No stored messages available.");
                    } else {
                        System.out.println("Stored messages:");
                        displayReport(storedMessages);
                    }
                }
                case 4 -> {
                    System.out.print("Enter message ID to search: ");
                    String msgID = scanner.nextLine();
                    searchByMessageID(sentMessages, storedMessages, msgID);
                }
                case 5 -> {
                    System.out.print("Enter recipient number to search: ");
                    String recipient = scanner.nextLine();
                    ArrayList<Message> matches = searchByRecipient(sentMessages, storedMessages, disregardedMessages, recipient);
                    if (matches.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        System.out.println("Search results for recipient: " + recipient);
                        displayReport(matches);
                    }
                }
                case 6 -> {
                    System.out.print("Enter message hash to delete: ");
                    String hash = scanner.nextLine();
                    boolean deleted = deleteByHash(sentMessages, storedMessages, hash, scanner);
                    if (deleted) {
                        System.out.println("Message was deleted successfully.");
                    } else {
                        System.out.println("No message matched that hash.");
                    }
                }
                case 7 -> {
                    ArrayList<Message> allMessages = new ArrayList<>();
                    allMessages.addAll(sentMessages);
                    allMessages.addAll(storedMessages);
                    String longest = findLongestMessage(allMessages);
                    System.out.println("Longest message: " + longest);
                }
                case 8 -> {
                    ArrayList<Message> allMessages = new ArrayList<>();
                    allMessages.addAll(sentMessages);
                    allMessages.addAll(storedMessages);
                    displaySenderAndRecipient(allMessages, loggedInUsername);
                }
                case 9 -> {
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages to report.");
                    } else {
                        displayReport(sentMessages);
                    }
                }
                case 10 -> {
                    System.out.println("Total messages sent: " + sentMessages.size());
                    System.out.println("Thank you for using QuickChat!");
                    appRunning = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void sendMessages(Scanner scanner, ArrayList<Message> sentMessages,
                                    ArrayList<Message> storedMessages, ArrayList<Message> disregardedMessages) {
        System.out.print("How many messages would you like to send? ");
        int numMessages = readInt(scanner);

        if (numMessages <= 0) {
            System.out.println("Please enter a positive number of messages.");
            return;
        }

        for (int i = 1; i <= numMessages; i++) {
            System.out.println(" Message " + i);
            System.out.print("Enter recipient phone number: ");
            String recipient = scanner.nextLine();

            System.out.print("Enter message (max 250 characters): ");
            String messageText = scanner.nextLine();

            Message message = new Message(recipient, messageText);
            message.setMessageNumber(i);

            System.out.println(message.checkRecipientCell());
            System.out.println(message.checkMessageLength());

            // Only proceed if both recipient and message content are valid
            if (message.checkRecipientCell().contains("successfully") && message.checkMessageLength().contains("ready")) {
                message.createMessageHash();

                System.out.println("1) Send Message");
                System.out.println("2) Discard Message");
                System.out.println("3) Store Message for later");
                System.out.print("Choose an option: ");

                int sendChoice = readInt(scanner);
                String result = message.sentMessage(sendChoice);
                System.out.println(result);

                // Store message in appropriate list based on user choice
                switch (sendChoice) {
                    case 1:
                        sentMessages.add(message);
                        message.printMessage();
                        break;
                    case 2:
                        disregardedMessages.add(message);
                        break;
                    case 3:
                        storedMessages.add(message);
                        message.storeToJSON("stored_messages.json");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // Helper method to safely parse integer input with retry logic
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

    /**
     * Reads stored messages from JSON file.
     * Uses Google's Gson library to deserialize JSON back into Message objects.
     * Reference: https://github.com/google/gson
     */
    public static ArrayList<Message> readStoredMessagesFromJSON(String filename) {
        ArrayList<Message> messages = new ArrayList<>();
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            Gson gson = new Gson();
            
            String[] jsonLines = content.trim().split("\n");
            for (String line : jsonLines) {
                if (!line.isEmpty()) {
                    Message msg = gson.fromJson(line, Message.class);
                    messages.add(msg);
                }
            }
            System.out.println("Loaded " + messages.size() + " stored messages from file.");
        } catch (Exception e) {
            System.out.println("No stored messages file found or error reading: " + e.getMessage());
        }
        
        return messages;
    }

    /**
     * Finds and returns the longest message from a list of messages.
     * Part 3 Feature: Display Longest Stored Message
     */
    public static String findLongestMessage(ArrayList<Message> messages) {
        if (messages.isEmpty()) {
            return "No stored messages.";
        }
        
        Message longest = messages.get(0);
        for (Message msg : messages) {
            if (msg.getMessageContent().length() > longest.getMessageContent().length()) {
                longest = msg;
            }
        }
        
        return longest.getMessageContent();
    }

    /**
     * Searches for a message by ID and returns recipient and message content.
     * Part 3 Feature: Search for Message ID and Return Recipient + Message
     */
    public static void searchByMessageID(ArrayList<Message> sentMessages, 
                                        ArrayList<Message> storedMessages, 
                                        String messageID) {
        // Check sent messages
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(messageID)) {
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageContent());
                return;
            }
        }
        
        // Check stored messages
        for (Message msg : storedMessages) {
            if (msg.getMessageID().equals(messageID)) {
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageContent());
                return;
            }
        }
        
        System.out.println("Message ID not found.");
    }

    /**
     * Displays sender and recipient information for all messages.
     * Part 3 Feature: Display Sender and Recipient of Stored Messages
     */
    public static void displaySenderAndRecipient(ArrayList<Message> messages, String senderUsername) {
        System.out.println(" Sender and Recipient Report ");
        for (Message msg : messages) {
            System.out.println("Sender: " + senderUsername);
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("---");
        }
    }

    // Search through all message lists for matching recipient
    public static ArrayList<Message> searchByRecipient(ArrayList<Message> sentMessages, 
                                                       ArrayList<Message> storedMessages, 
                                                       ArrayList<Message> disregardedMessages,
                                                       String recipient) {
        ArrayList<Message> results = new ArrayList<>();
        
        // Check sent messages
        for (Message message : sentMessages) {
            if (message.getRecipient() != null && message.getRecipient().equals(recipient)) {
                results.add(message);
            }
        }
        
        // Check stored messages
        for (Message message : storedMessages) {
            if (message.getRecipient() != null && message.getRecipient().equals(recipient)) {
                results.add(message);
            }
        }
        
        // Check disregarded messages
        for (Message message : disregardedMessages) {
            if (message.getRecipient() != null && message.getRecipient().equals(recipient)) {
                results.add(message);
            }
        }
        
        return results;
    }

    // Search for a message by its hash and delete if user confirms
    public static boolean deleteByHash(ArrayList<Message> sentMessages, 
                                       ArrayList<Message> storedMessages,
                                       String hash,
                                       Scanner scanner) {
        
        // Check sent messages first
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash() != null && sentMessages.get(i).getMessageHash().equals(hash)) {
                System.out.print("Are you sure you want to delete this message? (yes/no): ");
                String confirmation = scanner.nextLine().toLowerCase();
                
                if (confirmation.equals("yes")) {
                    sentMessages.remove(i);
                    return true;
                } else {
                    System.out.println("Deletion cancelled.");
                    return false;
                }
            }
        }
        
        // Check stored messages second
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash() != null && storedMessages.get(i).getMessageHash().equals(hash)) {
                System.out.print("Are you sure you want to delete this message? (yes/no): ");
                String confirmation = scanner.nextLine().toLowerCase();
                
                if (confirmation.equals("yes")) {
                    storedMessages.remove(i);
                    return true;
                } else {
                    System.out.println("Deletion cancelled.");
                    return false;
                }
            }
        }
        
        return false;
    }

    // Display message details in a formatted report
    public static void displayReport(ArrayList<Message> messages) {
        System.out.println(" Message Report ");
        for (Message message : messages) {
            System.out.println("Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessageContent());          
        }
    }
}