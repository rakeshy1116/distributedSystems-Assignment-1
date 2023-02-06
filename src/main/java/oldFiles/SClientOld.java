package oldFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SClientOld {
    public static void main(String[] args) throws IOException {
        try {
            Socket clientSocket = new Socket("127.0.0.1", 5555);
            PrintWriter printer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader inputer = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = inputer.readLine();
            while(!message.equals("logout")) {
                printer.println(message);
                String respone = socketIn.readLine();
                if(respone.equals("off"))
                {
                    break;
                }
                message = inputer.readLine();
//
                //System.out.println(socketIn.readLine());
            }
            inputer.close();
            printer.close();
            socketIn.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
