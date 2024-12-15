package MultipleClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;

class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            username = in.readLine();
            if (username == null || username.isBlank() || Server.getBannedPhrases().stream().anyMatch(username::contains)) {
                out.println("REJECT: Username contains banned phrases or is invalid.");
                System.out.println("Rejected connection for prohibited username: " + username);
                socket.close();
                return;
            }

            System.out.println(username + " joined.");
            Server.broadcast(username + " has joined the chat.", this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) {
                    handleSlashCommands(message);
                } else if (message.startsWith("@")) {
                    if (!handleAtCommands(message)) {
                        out.println("Invalid command. Type /help for available commands.");
                    }
                } else if (Server.getBannedPhrases().stream().anyMatch(message::contains)) {
                    out.println("Your message contains a banned phrase and cannot be sent.");
                } else {
                    Server.broadcast(username + ": " + message, this);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            socket.close();
            Server.removeClient(this);
            Server.broadcast(username + " has left the chat.", null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleSlashCommands(String message) {
        switch (message) {
            case "/list":
                out.println("Connected users: " + String.join(", ", Server.getUsernames()));
                break;
            case "/banned":
                out.println("Banned phrases: " + String.join(", ", Server.getBannedPhrases()));
                break;
            default:
                out.println("Unknown command. Type /help for available commands.");
        }
    }

    private boolean handleAtCommands(String message) {
        if (message.startsWith("@allexc-")) {
            String[] parts = message.split(" ", 2);
            if (parts.length < 2) {
                out.println("Invalid syntax. Use: @allexc-username1-username2 message");
                return true;
            }

            String[] excludedUsernames = parts[0].substring(8).split("-");
            String broadcastMessage = parts[1];

            synchronized (Server.getClientHandlers()) {
                for (ClientHandler client : Server.getClientHandlers()) {
                    if (client != this && Arrays.stream(excludedUsernames).noneMatch(client.getUsername()::equals)) {
                        client.sendMessage("[Broadcast] " + username + ": " + broadcastMessage);
                    }
                }
            }
            out.println("Message sent to everyone except: " + String.join(", ", excludedUsernames));
            return true;
        } else if (message.startsWith("@")) {
            String[] parts = message.split(" ", 2);
            String[] targetUsernames = parts[0].substring(1).split(",");
            String groupMessage = parts.length > 1 ? parts[1] : "";

            boolean foundAtLeastOne = false;

            for (String targetUsername : targetUsernames) {
                Optional<ClientHandler> targetClient = Server.getClientByUsername(targetUsername.trim());
                if (targetClient.isPresent()) {
                    targetClient.get().sendMessage("[Private] " + username + ": " + groupMessage);
                    foundAtLeastOne = true;
                } else {
                    out.println("User " + targetUsername.trim() + " not found.");
                }
            }

            return foundAtLeastOne;
        }
        return false;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return username;
    }
}