

package st10537683.part1;

import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Messages {

    public String messageID;
    public String recipient;
    public String message;

    // Arrays for sent and stored messages
    static String[] sentMessages = new String[100];
    static int totalMessages = 0;

    static String[] storedMessages = new String[100];
    static int storedCount = 0;

    // JSON file
    private static final Path JSON_FILE =
            Paths.get("messages.json");

    // Constructor
    public Messages(String messageID, String recipient,
                    String message) {

        this.messageID = messageID;
        this.recipient = recipient;
        this.message = message;
    }

    // Validate message ID
    public boolean checkMessageID() {
        return messageID != null
                && messageID.length() <= 10;
    }

    // Validate recipient cellphone number
    public boolean checkRecipientCell() {
        return recipient != null
                && recipient.matches("\\+\\d{11,13}");
    }

    // Create message hash
    public String createMessageHash(int messageNumber) {

        String trimmedMessage = message.trim();

        if (trimmedMessage.isEmpty()) {
            return messageID.substring(0, 2)
                    + ":" + messageNumber + ":EMPTY";
        }

        String[] words = trimmedMessage.split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2)
                + ":" + messageNumber + ":"
                + (firstWord + lastWord).toUpperCase();
    }

    // Send, disregard or store a message
    public String sentMessage(int choice, int messageNumber,
                              String messageHash) {

        switch (choice) {

            case 1:
                if (totalMessages >= sentMessages.length) {
                    return "Sent message storage is full.";
                }

                sentMessages[totalMessages] = message;
                totalMessages++;

                return "Message successfully sent.";

            case 2:
                return "Message disregarded.";

            case 0:
                if (storedCount >= storedMessages.length) {
                    return "Stored message storage is full.";
                }

                // Save message details to JSON first
                boolean saved = storeMessage(
                        messageID,
                        messageNumber,
                        recipient,
                        message,
                        messageHash
                );

                if (!saved) {
                    return "Message could not be stored.";
                }

                storedMessages[storedCount] = message;
                storedCount++;

                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    // Display sent messages
    public static String printMessages() {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < totalMessages; i++) {
            result.append(i + 1)
                    .append(". ")
                    .append(sentMessages[i])
                    .append("\n");
        }

        return result.toString();
    }

    // Return number of sent messages
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Display stored messages
    public static String printStoredMessages() {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < storedCount; i++) {
            result.append(i + 1)
                    .append(". ")
                    .append(storedMessages[i])
                    .append("\n");
        }

        return result.toString();
    }

    // Escape JSON special characters
    private static String escapeJSON(String text) {

        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        result.append(String.format(
                                "\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
            }
        }

        return result.toString();
    }

    // Save message to messages.json as a JSON array
    public static boolean storeMessage(
            String messageID,
            int messageNumber,
            String recipient,
            String message,
            String messageHash) {

        try {
            String existingJSON = "[]";

            if (Files.exists(JSON_FILE)) {
                existingJSON = new String(
                        Files.readAllBytes(JSON_FILE),
                        StandardCharsets.UTF_8
                ).trim();

                if (existingJSON.isEmpty()) {
                    existingJSON = "[]";
                }
            }

            if (!existingJSON.startsWith("[")
                    || !existingJSON.endsWith("]")) {
                System.out.println(
                        "Invalid JSON format in messages.json."
                );
                return false;
            }

            String object = "{\n"
                    + "  \"MessageID\": \""
                    + escapeJSON(messageID) + "\",\n"
                    + "  \"MessageNumber\": "
                    + messageNumber + ",\n"
                    + "  \"Recipient\": \""
                    + escapeJSON(recipient) + "\",\n"
                    + "  \"Message\": \""
                    + escapeJSON(message) + "\",\n"
                    + "  \"MessageHash\": \""
                    + escapeJSON(messageHash) + "\"\n"
                    + "}";

            String content = existingJSON.substring(
                    1, existingJSON.length() - 1
            ).trim();

            String updatedJSON;

            if (content.isEmpty()) {
                updatedJSON = "[\n" + object + "\n]";
            } else {
                updatedJSON = "[\n"
                        + content + ",\n"
                        + object + "\n]";
            }

            Files.write(
                    JSON_FILE,
                    updatedJSON.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println(
                    "Message saved to messages.json."
            );

            return true;

        } catch (IOException e) {
            System.out.println(
                    "Error writing JSON file: " + e.getMessage()
            );
            return false;
        }
    }

    // QuickChat menu
    public static void startQuickChat() {

        Scanner scanner = new Scanner(System.in);
        int messageNumber = 0;

        System.out.println("\nWelcome to QuickChat!");

        System.out.print(
                "How many messages would you like to send? "
        );

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
            return;
        }

        int maxMessages = scanner.nextInt();
        scanner.nextLine();

        if (maxMessages < 0) {
            System.out.println(
                    "The number of messages cannot be negative."
            );
            return;
        }

        int menuChoice = 0;

        while (menuChoice != 3) {

            System.out.println("\n=== QUICKCHAT MENU ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid option.");
                scanner.nextLine();
                continue;
            }

            menuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (menuChoice) {

                case 1:
                    if (messageNumber >= maxMessages) {
                        System.out.println(
                                "You have reached your message limit."
                        );
                        break;
                    }

                    System.out.print("Enter recipient number: ");
                    String recipient = scanner.nextLine();

                    System.out.print("Enter your message: ");
                    String messageText = scanner.nextLine();

                    if (messageText.length() > 250) {
                        System.out.println(
                                "Message must not exceed 250 characters."
                        );
                        break;
                    }

                    // Generate a random 10-digit message ID
                    long randomID =
                            (long) (Math.random()
                            * 9000000000L) + 1000000000L;

                    String id = String.valueOf(randomID);

                    Messages msg = new Messages(
                            id, recipient, messageText
                    );

                    if (!msg.checkMessageID()) {
                        System.out.println("Invalid message ID.");
                        break;
                    }

                    if (!msg.checkRecipientCell()) {
                        System.out.println(
                                "Invalid recipient number. Enter + "
                                + "followed by 11 to 13 digits."
                        );
                        break;
                    }

                    int currentNumber = messageNumber + 1;

                    String hash =
                            msg.createMessageHash(currentNumber);

                    System.out.println("\n=== MESSAGE DETAILS ===");
                    System.out.println("Message ID: " + id);
                    System.out.println(
                            "Message Number: " + currentNumber
                    );
                    System.out.println("Recipient: " + recipient);
                    System.out.println("Message: " + messageText);
                    System.out.println("Message Hash: " + hash);

                    System.out.println("\n1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("0) Store Message to send later");
                    System.out.print("Choose option: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid option.");
                        scanner.nextLine();
                        break;
                    }

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println(
                            msg.sentMessage(
                                    choice, currentNumber, hash
                            )
                    );

                    if (choice == 1 || choice == 0) {
                        messageNumber++;
                    }

                    break;

                case 2:
                    System.out.println("\n=== SENT MESSAGES ===");

                    if (totalMessages == 0) {
                        System.out.println(
                                "No messages have been sent."
                        );
                    } else {
                        System.out.println(printMessages());
                    }

                    System.out.println(
                            "Total messages sent: "
                            + returnTotalMessages()
                    );
                    break;

                case 3:
                    System.out.println(
                            "Thank you for using QuickChat."
                    );
                    break;

                case 4:
                    System.out.println("\n=== STORED MESSAGES ===");

                    if (storedCount == 0) {
                        System.out.println(
                                "No messages stored this session."
                        );
                    } else {
                        System.out.println(printStoredMessages());
                    }
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Standalone entry point for testing Messages
    public static void main(String[] args) {
        startQuickChat();
    }
}
