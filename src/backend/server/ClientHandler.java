package backend.server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private static int clientsCount = 0;
    private Server server;
    private Socket clientSocket;
    private PrintWriter outMessage;
    private Scanner inMessage;
    public String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public ClientHandler(Socket clientSocket, Server server) {
        try {
            clientsCount++;
            this.server = server;
            this.clientSocket = clientSocket;
            this.outMessage = new PrintWriter(clientSocket.getOutputStream());
            this.inMessage = new Scanner(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }


    }

    private void close() {
        server.removeClientFromServer(this);
        clientsCount--;
        server.sendMsgToAllClients("In our chat client count is " + clientsCount);
    }

    public void sendMsg(String msg) {
        outMessage.println(msg);
        outMessage.flush();

    }

    @Override
    public void run() {

        try {
            server.sendMsgToAllClients("We have a new clients");

            while (true) {
                if (inMessage.hasNext()) {
                    String clientMsg = inMessage.nextLine();
                    System.out.println(clientMsg);

                    if (clientMsg.equalsIgnoreCase("EXIT")) {
                        break;
                    }
                    if (clientMsg.startsWith("/w")) {
                        server.sendMsgToClient(clientMsg);
                    } else {
                        server.sendMsgToAllClients(clientMsg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }


    }
}
