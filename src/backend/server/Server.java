package backend.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    static final int PORT = 8666;
    List<ClientHandler> clients = new ArrayList<>();

    public Server() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is launched");

            while (true) {
                clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Server finished his work");
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void sendMsgToAllClients(String msg) {
        for (ClientHandler client : clients) {
            client.sendMsg(msg);
        }
    }

    public void sendMsgToClient(String msg) {
        String[] parts = msg.split("\\s");
        String[] finalMsg = new String[parts.length - 2];

        for (int i = 0; i < parts.length - 2; i++) {
            finalMsg[i] = parts[i + 2];
        }
        String str = Arrays.toString(finalMsg);

        for (ClientHandler client : clients) {
            if (client.getNick().equals(parts[1])) {
                client.sendMsg(str);
            }
        }


    }

    public void removeClientFromServer(ClientHandler client) {
        clients.remove(client);
    }
}
