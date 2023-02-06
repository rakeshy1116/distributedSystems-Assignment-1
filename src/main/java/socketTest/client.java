package socketTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws IOException {
        try {
            Socket clientSocket = new Socket("127.0.0.1", 5555);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String message = in.readLine();
            out.println(message);
            in.close();
            out.close();
            clientSocket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
