package progassmentcom.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class Message {
    private static final String RECIPIENT_PATTERN = "^\\+27\\d{9}$";

    String messageID;
    int messageNumber;
    String recipient;
    String messageContent;
    String messageHash;
    boolean messageSent;
    boolean messageReceived;
    boolean messageRead;

    public Message(String recipient, String messageContent) {
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.messageNumber = 0;
        this.messageSent = false;
        this.messageReceived = false;
        this.messageRead = false;
        this.messageHash = "";

        Random random = new Random();
        this.messageID = String.format("%010d", random.nextInt(1_000_000_000));
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{1,10}");
    }

    public boolean isRecipientValid() {
        return recipient != null && recipient.matches(RECIPIENT_PATTERN);
    }

    public String checkRecipientCell() {
        if (isRecipientValid()) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        String firstTwoDigits = (messageID != null && messageID.length() >= 2)
                ? messageID.substring(0, 2)
                : "00";

        String[] words = (messageContent == null || messageContent.trim().isEmpty())
                ? new String[]{"MESSAGE"}
                : messageContent.trim().split("\\s+");

        String firstWord = normaliseWord(words[0]);
        String lastWord = normaliseWord(words[words.length - 1]);
        if (firstWord.isEmpty()) {
            firstWord = "MESSAGE";
        }
        if (lastWord.isEmpty()) {
            lastWord = "MESSAGE";
        }

        messageHash = firstTwoDigits + ":" + messageNumber + ":" + firstWord.toUpperCase() + lastWord.toUpperCase();
        return messageHash;
    }

    private String normaliseWord(String word) {
        return word == null ? "" : word.replaceAll("[^A-Za-z0-9?]", "");
    }

    public boolean isMessageLengthValid() {
        return messageContent != null && messageContent.length() <= 250;
    }

    public String checkMessageLength() {
        if (isMessageLengthValid()) {
            return "Message ready to send.";
        }

        int exceededBy = (messageContent == null) ? 250 : messageContent.length() - 250;
        return "Message exceeds 250 characters by " + exceededBy + "; please reduce the size.";
    }

    public String sentMessage(int userChoice) {
        switch (userChoice) {
            case 1:
                messageSent = true;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    public String sentMessage() {
        return sentMessage(1);
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void storeMessage(String filename) {
        try (Writer writer = new FileWriter(filename, true)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(this));
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    public void storeToJSON(String filename) {
        storeMessage(filename);
    }

    public void printMessage() {
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageContent);
    }

    public String printMessages() {
        return "Message ID: " + messageID + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + messageContent;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageNumber(int num) {
        this.messageNumber = num;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public boolean isMessageSent() {
        return messageSent;
    }

    public void setMessageReceived(boolean received) {
        this.messageReceived = received;
    }

    public void setMessageRead(boolean read) {
        this.messageRead = read;
    }

    public boolean isMessageReceived() {
        return messageReceived;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public int returnTotalMessages() {
        return 1;
    }
}
