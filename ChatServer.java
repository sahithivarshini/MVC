import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
 
public class ChatServer {
    private static final int PORT = 12345;
    private static Set<String> connectedClients = new HashSet<>();
    private static Set<PrintWriter> clientWriters = new HashSet<>();
 
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Server listening on port " + PORT);
 
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    private static class ClientHandler extends Thread {
        private String clientName;
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
 
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
 
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
 
                clientName = reader.readLine();
                System.out.println("New connection from " + clientName);
                synchronized (connectedClients) {
                    connectedClients.add(clientName);
                }
 
                broadcastConnectedUsers();
 
                clientWriters.add(writer);
 
                String message;
                while ((message = reader.readLine()) != null) {
                    broadcastMessage(clientName + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Client " + clientName + " has disconnected");
            } finally {
                if (clientName != null) {
                    synchronized (connectedClients) {
                        connectedClients.remove(clientName);
                    }
                    clientWriters.remove(writer);
                    broadcastConnectedUsers();
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        private void broadcastMessage(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
 
        private void broadcastConnectedUsers() {
            StringBuilder userList = new StringBuilder("[ConnectedUsers]");
            for (String user : connectedClients) {
                userList.append(user).append(",");
            }
            for (PrintWriter writer : clientWriters) {
                writer.println(userList.toString());
            }
        }
    }
}
