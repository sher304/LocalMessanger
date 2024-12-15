package MultipleClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the server address: ");
            String serverAddress = scanner.nextLine();
            System.out.println("Enter the server port: ");
            int port = scanner.nextInt();
            scanner.nextLine();
            Socket socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to the server.\nEnter your username:");
            String username = scanner.nextLine();
            out.println(username);

            String serverResponse = in.readLine();
            if (serverResponse != null && serverResponse.startsWith("REJECT")) {
                System.out.println("Server rejected your username: " + serverResponse.substring(7));
                socket.close();
                scanner.close();
                return;
            }

            System.out.println("Welcome to the chat! Type '/help' to see available commands.");

            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Connection to the server was lost.");
                }
            });
            readerThread.start();

            System.out.println("Type your messages below:");
            String message;
            while (!(message = scanner.nextLine()).equalsIgnoreCase("quit")) {
                if (message.equals("/help")) {
                    System.out.println("Commands:");
                    System.out.println("/list - List all connected users");
                    System.out.println("@username message - Send a private message");
                    System.out.println("@username1,username2 message - Send a message to multiple users");
                    System.out.println("@allexc-username1-username2 message - Send a message to everyone except specific users");
                    System.out.println("quit - Exit the chat");
                } else if (message.equals("/list")) {
                    out.println("/list");
                } else if (message.equals("/banned")) {
                    out.println("/banned");
                } else {
                    if(!message.isEmpty()) out.println(message);
                }
            }

            socket.close();
            System.out.println("You have left the chat.");
        } catch (IOException e) {
            System.out.println("An error occurred while connecting to the server.");
        }
    }
}