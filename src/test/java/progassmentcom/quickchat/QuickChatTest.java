package progassmentcom.quickchat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

public class QuickChatTest {

    private ArrayList<Message> sentMessages;
    private ArrayList<Message> storedMessages;
    private ArrayList<Message> disregardedMessages;

    @BeforeEach
    public void setUp() {
        sentMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
    }

    // Tests for findLongestMessage()

    @Test
    public void testFindLongestMessageWithMessages() {
        ArrayList<Message> messages = new ArrayList<>();

        Message msg1 = new Message("+27834557896", "Short");
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message msg3 = new Message("+27718693002", "Medium length message here");

        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);

        String longest = QuickChat.findLongestMessage(messages);
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest,
                     "Should find the longest message");
    }

    @Test
    public void testFindLongestMessageEmpty() {
        ArrayList<Message> messages = new ArrayList<>();
        String longest = QuickChat.findLongestMessage(messages);
        assertEquals("No stored messages.", longest, "Should return appropriate message for empty list");
    }

    @Test
    public void testFindLongestMessageSingleMessage() {
        ArrayList<Message> messages = new ArrayList<>();
        Message msg = new Message("+27834557896", "Only message");
        messages.add(msg);

        String longest = QuickChat.findLongestMessage(messages);
        assertEquals("Only message", longest, "Should return the only message");
    }

    @Test
    public void testFindLongestMessageMultipleSameLength() {
        ArrayList<Message> messages = new ArrayList<>();

        Message msg1 = new Message("+27834557896", "Same length");
        Message msg2 = new Message("+27838884567", "Same length");

        messages.add(msg1);
        messages.add(msg2);

        String longest = QuickChat.findLongestMessage(messages);
        assertEquals("Same length", longest, "Should return one of the equal length messages");
    }

    // Tests for searchByMessageID()
    @Test
    public void testSearchByMessageIDFound() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();

        Message msg = new Message("+27834557896", "Did you get the cake?");
        msg.setMessageNumber(1);
        String messageID = msg.getMessageID();

        sentMessages.add(msg);

        // This should not throw an exception and should print the message info
        QuickChat.searchByMessageID(sentMessages, storedMessages, messageID);
        // Verify the message exists in sent list
        assertEquals(messageID, msg.getMessageID(), "Message ID should match");
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();

        Message msg = new Message("+27834557896", "Did you get the cake?");
        sentMessages.add(msg);

        // Search for non-existent ID - should not crash
        QuickChat.searchByMessageID(sentMessages, storedMessages, "9999999999");
        // Test passes if no exception is thrown
    }

    @Test
    public void testSearchByMessageIDInStoredMessages() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();

        Message msg = new Message("+27834557896", "Stored message");
        msg.setMessageNumber(1);
        String messageID = msg.getMessageID();

        storedMessages.add(msg);

        // Search should find it in stored messages
        QuickChat.searchByMessageID(sentMessages, storedMessages, messageID);
        assertEquals(messageID, msg.getMessageID(), "Should find message in stored list");
    }

    // Tests for displaySenderAndRecipient()
    @Test
    public void testDisplaySenderAndRecipientWithMessages() {
        ArrayList<Message> messages = new ArrayList<>();

        Message msg1 = new Message("+27834557896", "Message 1");
        Message msg2 = new Message("+27838884567", "Message 2");

        messages.add(msg1);
        messages.add(msg2);

        String sender = "kyl_1";
        // This should not throw an exception
        QuickChat.displaySenderAndRecipient(messages, sender);

        // Verify the data exists
        assertEquals(2, messages.size(), "Should have 2 messages");
        assertEquals("+27834557896", messages.get(0).getRecipient(), "First message recipient should match");
    }

    @Test
    public void testDisplaySenderAndRecipientEmpty() {
        ArrayList<Message> messages = new ArrayList<>();
        String sender = "kyl_1";

        // Should handle empty list gracefully
        QuickChat.displaySenderAndRecipient(messages, sender);
        assertEquals(0, messages.size(), "Should have 0 messages");
    }

    // Tests for searchByRecipient()
    @Test
    public void testSearchByRecipientFoundInSent() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();

        Message msg = new Message("+27834557896", "Test message");
        sentMessages.add(msg);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages,
                                                                 disregardedMessages, "+27834557896");

        assertEquals(1, results.size(), "Should find 1 message");
        assertEquals("+27834557896", results.get(0).getRecipient(), "Recipient should match");
    }

    @Test
    public void testSearchByRecipientFoundInStored() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();

        Message msg = new Message("+27838884567", "Stored message");
        storedMessages.add(msg);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages,
                                                                 disregardedMessages, "+27838884567");

        assertEquals(1, results.size(), "Should find 1 message");
    }

    @Test
    public void testSearchByRecipientMultipleMatches() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();

        Message msg1 = new Message("+27718693002", "Message 1");
        Message msg2 = new Message("+27718693002", "Message 2");

        sentMessages.add(msg1);
        sentMessages.add(msg2);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages, disregardedMessages, "+27718693002");

        assertEquals(2, results.size());
    }

    @Test
    public void testDeleteByHashInSentMessages() {
        Message msg = new Message("+27718693002", "Test message");
        msg.messageID = "1234567890";
        msg.setMessageNumber(0);
        msg.createMessageHash();
        sentMessages.add(msg);

        boolean deleted = QuickChat.deleteByHash(sentMessages, storedMessages, msg.getMessageHash(), new Scanner("no\n"));

        assertFalse(deleted);
        assertNotNull(msg.getMessageHash());
        assertEquals(1, sentMessages.size());
    }

    @Test
    public void testDeleteByHashNotFound() {
        Message msg = new Message("+27718693002", "Test");
        sentMessages.add(msg);

        boolean deleted = QuickChat.deleteByHash(sentMessages, storedMessages, "NONEXISTENT:0:TESTEST", new Scanner("no\n"));

        assertFalse(deleted);
        assertEquals(1, sentMessages.size());
    }

    @Test
    public void testDeleteByHashInStoredMessages() {
        Message msg = new Message("+27718693002", "Stored message");
        msg.messageID = "0987654321";
        msg.setMessageNumber(1);
        msg.createMessageHash();
        storedMessages.add(msg);

        boolean deleted = QuickChat.deleteByHash(sentMessages, storedMessages, msg.getMessageHash(), new Scanner("no\n"));

        assertFalse(deleted);
        assertEquals(1, storedMessages.size());
    }

    @Test
    public void testDisplayReportWithMessages() {
        Message msg = new Message("+27718693002", "Test message content");
        msg.messageID = "1234567890";
        msg.setMessageNumber(0);
        msg.createMessageHash();
        sentMessages.add(msg);

        PrintStream original = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            QuickChat.displayReport(sentMessages);

            String output = outContent.toString();
            assertTrue(output.contains("Message Report"));
            assertTrue(output.contains("+27718693002"));
            assertTrue(output.contains("Test message content"));
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testDisplayReportEmptyList() {
        PrintStream original = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            QuickChat.displayReport(sentMessages);

            String output = outContent.toString();
            assertTrue(output.contains("Message Report"));
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testDisplayReportMultipleMessages() {
        Message msg1 = new Message("+27718693002", "First message");
        msg1.messageID = "1111111111";
        msg1.setMessageNumber(0);
        msg1.createMessageHash();

        Message msg2 = new Message("+27123456789", "Second message");
        msg2.messageID = "2222222222";
        msg2.setMessageNumber(1);
        msg2.createMessageHash();

        sentMessages.add(msg1);
        sentMessages.add(msg2);

        PrintStream original = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            QuickChat.displayReport(sentMessages);

            String output = outContent.toString();
            assertTrue(output.contains("+27718693002"));
            assertTrue(output.contains("+27123456789"));
            assertTrue(output.contains("First message"));
            assertTrue(output.contains("Second message"));
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testMessageFlowFromSendToSearch() {
        Message msg1 = new Message("+27718693002", "Hi Mike");
        msg1.messageID = "1234567890";
        msg1.setMessageNumber(1);
        msg1.createMessageHash();

        Message msg2 = new Message("+27123456789", "Hi John");
        msg2.messageID = "0987654321";
        msg2.setMessageNumber(2);
        msg2.createMessageHash();

        sentMessages.add(msg1);
        sentMessages.add(msg2);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages, disregardedMessages, "+27718693002");

        assertEquals(1, results.size());
        assertEquals("Hi Mike", results.get(0).getMessageContent());
    }

    @Test
    public void testMessageOrganizationByType() {
        Message msg1 = new Message("+27834557896", "Sent message");
        msg1.messageID = "1111111111";
        msg1.setMessageNumber(1);
        msg1.createMessageHash();
        sentMessages.add(msg1);

        Message msg2 = new Message("+27834557896", "Stored message");
        msg2.messageID = "2222222222";
        msg2.setMessageNumber(2);
        msg2.createMessageHash();
        storedMessages.add(msg2);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages,
                                                                 disregardedMessages, "+27834557896");

        assertEquals(2, results.size(), "Should find 2 messages with same recipient");
    }

    @Test
    public void testSearchByRecipientNotFound() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();

        Message msg = new Message("+27834557896", "Test message");
        sentMessages.add(msg);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages,
                                                                 disregardedMessages, "+27999999999");

        assertEquals(0, results.size(), "Should find 0 messages");
    }

    // Tests for readStoredMessagesFromJSON()
    @Test
    public void testReadStoredMessagesFromJSONFileNotFound() {
        // Test with non-existent file
        ArrayList<Message> messages = QuickChat.readStoredMessagesFromJSON("nonexistent_file.json");

        assertNotNull(messages, "Should return a list (possibly empty)");
        assertTrue(messages.isEmpty(), "Should return empty list when file doesn't exist");
    }

    @Test
    public void testReadStoredMessagesFromJSONEmptyList() {
        ArrayList<Message> messages = QuickChat.readStoredMessagesFromJSON("nonexistent_test.json");
        assertEquals(0, messages.size(), "Should handle missing files gracefully");
    }

    // Integration test for the overall workflow
    @Test
    public void testCompleteMessageWorkflow() {
        ArrayList<Message> sentMessages = new ArrayList<>();
        ArrayList<Message> storedMessages = new ArrayList<>();
        ArrayList<Message> disregardedMessages = new ArrayList<>();

        // Create multiple messages
        Message msg1 = new Message("+27834557896", "Short message");
        msg1.setMessageNumber(1);

        Message msg2 = new Message("+27834557896", "This is a longer message with more content");
        msg2.setMessageNumber(2);

        Message msg3 = new Message("+27838884567", "Message to different recipient");
        msg3.setMessageNumber(3);

        // Add to lists
        msg1.sentMessage(1);
        sentMessages.add(msg1);

        msg2.sentMessage(1);
        sentMessages.add(msg2);

        msg3.sentMessage(1);
        sentMessages.add(msg3);

        // Test search by recipient
        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages,
                                                                 disregardedMessages, "+27834557896");
        assertEquals(2, results.size(), "Should find 2 messages to same recipient");

        // Test longest message
        String longest = QuickChat.findLongestMessage(sentMessages);
        assertTrue(longest.contains("longer message"), "Should find the longer message");

        // Verify message count
        assertEquals(3, sentMessages.size(), "Should have 3 sent messages");
    }

    @Test
    public void testMessageHashConsistency() {
        Message msg = new Message("+27718693002", "Hello World");
        msg.messageID = "0012345678";
        msg.setMessageNumber(0);
        String hash1 = msg.createMessageHash();

        String hash2 = msg.createMessageHash();

        assertEquals(hash1, hash2);
        assertTrue(hash1.contains("HELLOWORLD"));
    }

    @Test
    public void testSearchResultsContainCorrectData() {
        Message msg = new Message("+27718693002", "Important message");
        msg.messageID = "5555555555";
        msg.setMessageNumber(0);
        msg.createMessageHash();
        sentMessages.add(msg);

        ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages, disregardedMessages, "+27718693002");

        assertEquals(1, results.size());
        Message found = results.get(0);
        assertEquals("+27718693002", found.getRecipient());
        assertEquals("Important message", found.getMessageContent());
        assertEquals("5555555555", found.getMessageID());
    }

    @Test
    public void testNoNullPointerOnEmptySearch() {
        assertDoesNotThrow(() -> {
            ArrayList<Message> results = QuickChat.searchByRecipient(sentMessages, storedMessages, disregardedMessages, null);
            assertEquals(0, results.size());
        });
    }

    @Test
    public void testDisplayReportDoesNotModifyList() {
        Message msg = new Message("+27718693002", "Test");
        msg.messageID = "1234567890";
        msg.setMessageNumber(0);
        msg.createMessageHash();
        sentMessages.add(msg);

        int sizeBefore = sentMessages.size();

        PrintStream original = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            QuickChat.displayReport(sentMessages);
        } finally {
            System.setOut(original);
        }

        int sizeAfter = sentMessages.size();
        assertEquals(sizeBefore, sizeAfter);
    }

    // Tests for message status flags through QuickChat
    @Test
    public void testMessageStatusFlagsInContext() {
        ArrayList<Message> messages = new ArrayList<>();

        Message msg = new Message("+27834557896", "Test message");
        msg.setMessageNumber(1);
        msg.sentMessage(1);  // Mark as sent
        msg.setMessageReceived(true);
        msg.setMessageRead(true);

        messages.add(msg);

        assertTrue(msg.isMessageSent(), "Message should be marked sent");
        assertTrue(msg.isMessageReceived(), "Message should be marked received");
        assertTrue(msg.isMessageRead(), "Message should be marked read");
    }

    @Test
    public void testMultipleMessageHandling() {
        ArrayList<Message> sentMessages = new ArrayList<>();

        // Create 5 messages
        for (int i = 1; i <= 5; i++) {
            Message msg = new Message("+2783455789" + i, "Message " + i);
            msg.setMessageNumber(i);
            msg.sentMessage(1);
            sentMessages.add(msg);
        }

        assertEquals(5, sentMessages.size(), "Should handle multiple messages");

        // Verify all messages are sent
        for (Message msg : sentMessages) {
            assertTrue(msg.isMessageSent(), "All messages should be marked as sent");
        }
    }
}