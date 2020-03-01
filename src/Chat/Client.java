package Chat;
import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    //Constructor del cliente, para poner ip y puerto
    public  Client(String address,int port){
        try{
            Socket socket = new Socket(address,port);
            System.out.println("Connected!");

            input = new DataInputStream(System.in);

            output = new DataOutputStream(socket.getOutputStream());

        }catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException i){
            System.out.println(i);

        }

        String line = ""; //Es el string que se va a leer del input

        while(!line.equals("Over")){
            try{
                line = input.readLine();
                output.writeUTF(line);
            }catch(IOException i){
                System.out.println(i);

            }
        }
        try{
            input.close();
            output.close();
            socket.close();
        }catch(IOException i){
            System.out.println(i);
        }
    }
    public static void main(String args []){
        Client client = new Client("192.168.0.14",5000);
    }

}
