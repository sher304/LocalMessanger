package MultipleClients;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    private static final List<String> bannedPhrases = new ArrayList<>();
    private static String serverAddress;
    private static int port;

    public static void main(String[] args) {
        readBannedWordsFile();
        readServerFile();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static synchronized Set<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public static synchronized List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        for (ClientHandler client : clientHandlers) {
            if (client.getUsername() != null) {
                usernames.add(client.getUsername());
            }
        }
        return usernames;
    }

    public static synchronized Optional<ClientHandler> getClientByUsername(String username) {
        return clientHandlers.stream()
                .filter(client -> username.equals(client.getUsername()))
                .findFirst();
    }

    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    public static List<String> getBannedPhrases() {
        return bannedPhrases;
    }

    private static void readBannedWordsFile() {
        try {
            File myObj = new File("bannedWords.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                bannedPhrases.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void readServerFile() {
        try {
            File myObj = new File("serverDetail.txt");
            Scanner myReader = new Scanner(myObj);
            serverAddress = myReader.nextLine();
            port = myReader.nextInt();
            System.out.println("The data: " + serverAddress + " " + port);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}

