import java.io.*;
import java.net.*;

public class ConnectionManager {
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ConnectionManager(String host, int port) throws IOException{
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }

    public void send(String message){
        out.println(message);
        out.flush();
    }

    public String receive() throws IOException{
        String str = in.readLine();
        return str;
    }
}
