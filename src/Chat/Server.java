package Chat;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.*;

/**
 * This class calls the Graphic Interface, creates a new instance of Server_Frame class
 */
public class Server  {
    /**
     * Sets the functionality of closing the JPanel
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Server_Frame my_server_frame = new Server_Frame();

        my_server_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

/**
 * In this class is where the constructor for the interface is set, even though it is just a blank JPanel for showing every client's activity
 * Also here is implemented the run() method which is the process that is always running, in this case it always has to be listening for a message, print it on a log and send it to the receiver.
 */
class Server_Frame extends JFrame implements Runnable {
    /**
     * This is the constructor, here every visual attribute for the interface is set.
     */
    public Server_Frame(){
        /**
         * sets the spawning coordinates and the size of the window.
         */
        setBounds(300,300,500,350);

        JPanel my_server_sheet = new JPanel();
        /**
         * Creates a new design for placing other UI objects
         */
        my_server_sheet.setLayout(new BorderLayout());
        /**
         * This is where all the activity in the server is going to show up
         */
        text_area = new JTextArea();
        /**
         * This sets the text_area in the middle of the Layout, this lets all the Panel to be used as the message shower
         */
        my_server_sheet.add(text_area,BorderLayout.CENTER);

        add(my_server_sheet);

        setVisible(true);
        /**
         * This creates the thread for keeping the Panel always running.
         */
        Thread frame_thread = new Thread(this);

        frame_thread.start();
    }


    @Override
    /**
     * This is where all the processes that have to be always running are.
     */
    public void run() {
        try {
            /**
             * This creates the socket rack that is going to be opened to connect the sending client to the receiving part of the server
             */
            ServerSocket socket_input = new ServerSocket(9999);
            /**
             * Declares the variables that are going to be used and creates a new Sendable_Package for the data that is about to get received.
             */
            String username,ip_address,message;

            Sendable_Package received_package;

            /**
             * Here is where all the data/message process is made; including data input, management of that data and then the dta output again.
             */
            while(true)  {
                /**
                 * This opnes the connection between the socket and the server socket.
                 */
                Socket in_socket = socket_input.accept();
                /**
                 * Creates an ObjectInputStream for receiving the data package the sending client sent.
                 */
                 ObjectInputStream data_package = new ObjectInputStream(in_socket.getInputStream());
                /**
                 * Again, this is made for casting.
                 * It turns the data package, which is in bit code, and turns it by force to a Sendable_Package to be able to manage its info as strings.
                 */
                 received_package = (Sendable_Package) data_package.readObject();
                /**
                 * This uses the getters from the Sendable_Package class
                 */
                username = received_package.getNick();

                 ip_address = received_package.getIp();

                 message = received_package.getMessage();
                /**
                 * This is for making a log of the activity between clients inside the server.
                 */
                text_area.append("\n" + username +  " sent a message to >> " + ip_address + " the message was: " + message);

                /**
                 * THE FOLLOWING IS FOR THE RE-SEND PROCESS
                 */
                /**
                 * This creates a new socket which has to be on a different port from the other one, this is meant for the connection between the server an the receiving client.
                 * This uses the ip_address variable the sending client declared on the data_package; this means the message is exclusive for that wanted client.
                 */
                Socket sending_path = new Socket(ip_address,9090);
                /**
                 * This creates a new "way" or "stream" to send packages of data, this data is the same as the sending client declared, nothing is changed.
                 */
                ObjectOutputStream re_sendable_package = new ObjectOutputStream(sending_path.getOutputStream());
                /**
                 * It writes everything on the package received on a new package that is meant to be re-sent.
                 */
                re_sendable_package.writeObject(received_package);
                /**
                 * Every route of data is closed for order purposes
                 * Also as this is a loop it has to open-manage-send-close every time the message button is triggered.
                 */
                re_sendable_package.close();

                sending_path.close();

                in_socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the text_area object
     */
    private	JTextArea text_area;
}
