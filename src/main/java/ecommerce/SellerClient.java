package ecommerce;


import java.io.*;
import java.net.Socket;

public class SellerClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

        public static void main(String[] args) throws IOException {
            SellerClient client1 = new SellerClient();
            client1.startConnection("127.0.0.1", 7777);
            final long startTime = System.currentTimeMillis();


            File file = new File(args[0]);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String finalMessage = "";
            while ((st = br.readLine()) != null) {
                String msg1 = client1.sendMessage(st);
                finalMessage=msg1;
                System.out.println(msg1);
            }
            final long endTime = System.currentTimeMillis();
            System.out.println(finalMessage);
            System.out.println("Total execution time: " + (endTime - startTime));

    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
