package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args) throws IOException {
        System.out.println("Waiting for client");
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputClient = in.readLine();
            System.out.println(inputClient);
            in.close();
            out.close();
            serverSocket.close();
            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
