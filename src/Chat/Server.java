package Chat;
import java.net.*;
import java.io.*;


public class Server {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;

    //Constructor
    public Server(int port){
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started!");
            System.out.println("Waiting for client...");

            socket = server.accept();
            System.out.println("Client Accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";

            while (!line.equals("Over")){
                try{
                    line = in.readUTF();
                    System.out.println(line);
                }catch(IOException i){
                    System.out.println(i);
                }


            }
            System.out.println("Closing connection...");
            socket.close();
            in.close();

        }
        catch(IOException i){
            System.out.println(i);
        }
    }
    public static void main(String args []){
        Server server = new Server(5000);


    }

}
