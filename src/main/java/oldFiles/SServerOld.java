package oldFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SServerOld {
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(5555);
            while(true) {
                new SellerClientHandler(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SellerClientHandler extends Thread {

        private Socket clientSocket;
        private PrintWriter printer;
        private BufferedReader inputer;

        public SellerClientHandler(Socket socket) {
            this.clientSocket=socket;
        }

        public void run() {
            try {
                System.out.println("Connected to client");
                printer = new PrintWriter(clientSocket.getOutputStream(), true);
                inputer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = inputer.readLine()) != null) {
                    if ("logout".equals(inputLine)) {
                        break;
                    }
                    printer.println(inputLine);
                }
                inputer.close();
                printer.close();
//                serverSocket.close();
                clientSocket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        }

}
