# LocalMessenger Documentation

## Overview
LocalMessenger is a Java-based application that facilitates local communication among multiple clients through a server. It features direct messaging, multiple client connections, and a banned-words filter to ensure appropriate communication.

---

## Features

### 1. **Multiple Client Connections**
   - The server can handle multiple client connections simultaneously, enabling group communication.
   - Clients connect using socket programming, ensuring real-time data exchange.

### 2. **Direct Messaging**
   - Clients can send private messages to other specific users.
   - The system ensures private communication between users within the group.

### 3. **Banned Words Filter**
   - Messages containing prohibited words (defined in the `bannedWords.txt` file) are blocked.
   - This helps maintain a respectful and professional communication environment.

---

## System Architecture

### Components
1. **Server**
   - Manages client connections and message routing.
   - Uses the `Server.java` file to initialize and maintain server operations.

2. **Client**
   - Represents individual users in the system.
   - Uses `Client.java` to establish communication with the server.

3. **Client Handler**
   - Each connected client is assigned a `ClientHandler` instance.
   - This enables message processing and ensures isolation between clients.

---

## File Structure

1. **Server.java**
   - Implements the main server logic.
   - Manages client connection requests and routes messages appropriately.

2. **Client.java**
   - Handles the client-side interface and connection to the server.
   - Includes methods to send and receive messages.

3. **ClientHandler.java**
   - Manages interactions with individual clients.
   - Contains logic for message processing and broadcasting.

4. **bannedWords.txt**
   - Text file storing words that are restricted.
   - The server checks messages against this list to filter out inappropriate content.

5. **serverDetail.txt**
   - Stores configuration details for the server, such as IP address and port number.

---

## Installation

### Prerequisites
- Java Development Kit (JDK) installed on your machine.
- A text editor or IDE (e.g., IntelliJ IDEA, Eclipse).

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/sher304/LocalMessanger.git
   ```
2. Open the project in your IDE.
3. Compile the source files:
   ```bash
   javac Server.java Client.java ClientHandler.java
   ```
4. Run the server:
   ```bash
   java Server
   ```
5. Run the client(s):
   ```bash
   java Client
   ```

---

## Usage

1. Start the server using `Server.java`.
2. Launch `Client.java` for each user wanting to connect.
3. To send messages:
   - For group messages: Type the message and press enter.
   - For direct messages: Use the command `/dm <username> <message>`.
4. Messages containing banned words are blocked and not sent.

---

## Future Enhancements
- **User Authentication**: Add a login system to authenticate users.
- **Graphical User Interface (GUI)**: Create a more user-friendly interface.
- **Encryption**: Implement message encryption for enhanced security.
- **File Sharing**: Allow clients to share files.

---

## Contributing
1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit changes:
   ```bash
   git commit -m "Add feature-name"
   ```
4. Push to the branch:
   ```bash
   git push origin feature-name
   ```
5. Create a pull request on GitHub.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

## Acknowledgments
- Inspired by basic chat applications and socket programming concepts in Java.
- Special thanks to contributors and the open-source community.

