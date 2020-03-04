package Chat;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.*;

public class Server  {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Server_Frame my_server_frame = new Server_Frame();

        my_server_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class Server_Frame extends JFrame implements Runnable {

    public Server_Frame(){

        setBounds(300,300,500,350);

        JPanel my_server_sheet = new JPanel();

        my_server_sheet.setLayout(new BorderLayout());

        text_area = new JTextArea();

        my_server_sheet.add(text_area,BorderLayout.CENTER);

        add(my_server_sheet);

        setVisible(true);

        Thread frame_thread = new Thread(this);

        frame_thread.start();
    }


    @Override
    public void run() {
        try {
            ServerSocket socket_input = new ServerSocket(9999);

            String username,ip_address,message;

            Sendable_Package received_package;


            while(true)  {

                Socket in_socket = socket_input.accept();

                 ObjectInputStream data_package = new ObjectInputStream(in_socket.getInputStream());

                 received_package = (Sendable_Package) data_package.readObject();

                 username = received_package.getNick();

                 ip_address = received_package.getIp();

                 message = received_package.getMessage();

                text_area.append("\n" + username + ": " + message + " sent a message to >> " + ip_address);

                //SE VA A CREAR UN NUEVO SOCKET PARA EL PROCESO DE ENVIO DE DATOS AL OTRO CLIENTE

                Socket sending_path = new Socket(ip_address,9090);

                ObjectOutputStream re_sendable_package = new ObjectOutputStream(sending_path.getOutputStream());

                re_sendable_package.writeObject(received_package);

                re_sendable_package.close();

                sending_path.close();

                in_socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private	JTextArea text_area;
}
