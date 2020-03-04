
package Chat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;
import java.io.*;
import java.net.*;

/**
 * @Author José Antonio Espinoza, based on Pildoras Informáticas' source code.
 * This class is the one runnable, it starts a chain of class calling to complete the process.
 */
public class Cliente {
    /**
     * Main creates the frame GUI used for the client canvas.
     * Also, creates a new **instance** of a class ( in this case ClientFrame)
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        /**
         * .setDefaultCloseOperation works giving a command to the frame if you try to close it.
         * In this case I chose the close it definitively.
         */
        ClientFrame my_frame =new ClientFrame();

        my_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

/**
 * Created a new class, its function is to create the physical appearance for the client frame
 * @method extends gets methos from a library (JFrame); this is an example of **inheritance**.
 */
class ClientFrame extends JFrame{
    /**
     * setBounds is meant for giving the x,y where the canvas is going to be summoned; its width and height
     * Then created the instance "my_canvas" as a new object from the class "ClientFrame_Canvas"
     * Method add() places an instance on the ClientFrame.
     */
    public ClientFrame(){

        setBounds(0,0,600,430);

        ClientFrame_Canvas my_canvas = new ClientFrame_Canvas();

        getContentPane().setBackground(Color.darkGray);

        my_canvas.setBackground(Color.darkGray);

        add(my_canvas);

        setVisible(true);
    }

}

/**
 * This class creates what is necessary for the UI, and all the logic methods reserved for the Client class.
 * It extends JPanel to use Gui methods.
 * Implements Runnable for thread managing.
 */
class ClientFrame_Canvas extends JPanel implements Runnable{
    /**
     * This constructor is made for creating new instances of every UI objects used
     * Also, this constructor sets the main thread for the Client class.
     */
    public ClientFrame_Canvas(){

        JLabel nick_label =new JLabel("Username:");
        add(nick_label);

        nick_label.setForeground(Color.WHITE);
        nick_label.setFont(new Font("SANS_SERIF", Font.BOLD, 16));

        username_input = new JTextField(10);
        add(username_input);

        JLabel ip_label = new JLabel("Receiver's IP Address:");
        add(ip_label);

        ip_label.setForeground(Color.WHITE);
        ip_label.setFont(new Font("SANS_SERIF", Font.BOLD, 16));

        ip_input = new JTextField(15);
        add(ip_input);


        JLabel chat_label = new JLabel("\n" + "       CHAT:            ");
        add(chat_label);
        chat_label.setForeground(Color.WHITE);
        chat_label.setFont(new Font("SANS_SERIF", Font.BOLD, 22));

        chat_frame = new JTextArea(16,48);
        add(chat_frame);

        JLabel enter_text = new JLabel("Enter your message here " );
        add(enter_text);
        enter_text.setForeground(Color.WHITE);
        enter_text.setFont(new Font("SANS_SERIF", Font.BOLD, 14));

        JLabel arrows_text = new JLabel("↓ ↓ ↓" );
        add(arrows_text);
        arrows_text.setForeground(Color.WHITE);
        arrows_text.setFont(new Font("SANS_SERIF", Font.BOLD, 14));

        text_input =new JTextField(45);
        add(text_input);

        send_Button =new JButton("Send");


        Send_Text event = new Send_Text();

        send_Button.addActionListener(event);
        add(send_Button);

        Thread my_thread = new Thread(this);
        my_thread.start();


    }

    @Override
    /**
     * run() method is meant for a process you want always happening; in this case is the logic for a client to receive a data package.
     */
    public void run() {
        /**
         * try(), catch() is meant for executing a process and if it throws an exception it shows what happened in the Java shell.
         */
        try{
            /**
             * Creating a socket between the server and the receiving client, this has to be a different port as the socket connection for the sending client process and the server.
             * Also it makes a new instance of it to be able to manage it.
             */
            ServerSocket client_server = new ServerSocket(9090);

            Socket client;

            Sendable_Package received_package;

            while (true){
                /**
                 * .accept() is the one that generates the connection between both objects.
                 */
                client = client_server.accept();
                /**
                 * Used ObjectInputStream() because it is necessary to send multiple data between clients through the Server.
                 * This lets me work with input and output of packaged data.
                 */
                ObjectInputStream in_stream = new ObjectInputStream(client.getInputStream());
                /**
                 * Had to use "cast", which means to convert an object to something different by force.
                 * In this case the package flow is serialized, and I need to work it as a string, so casting it back to a Sendable_Package object lets me use the getters and setters again.
                 * Also, had to use .readObject() to see the contents of the dta package.
                 */
                received_package = (Sendable_Package) in_stream.readObject();
                /**
                 * This is just to make sure every client knows who is talking to them and what they sent.
                 */
                chat_frame.append("\n"+received_package.getNick()+": "+ received_package.getMessage());
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This class is meant for sending the data package to the server.
     * It implements ActionListener because it depends on the "Send" button.
     */
    private class Send_Text implements ActionListener{

        @Override
        /**
         * This void functionality is to:
         * 1) Print what you are sending, this to give continuity to the chat.
         * 2) Apply the socket creation, data pack-up and OutputStream creation for the info you want to send.
         * 3) Be called every time you press the "Send" button.
         */
        public void actionPerformed(ActionEvent actionEvent) {

            chat_frame.append("\n" + text_input.getText());

            try {
                /**
                 * Creates the socket between the client sending the message and the Server.
                 */
                Socket sending_socket =new Socket("192.168.0.14", 9999);
                /**
                 * Creates a new Sendable_Package instance which is the one we will create by getting all the information from the text fields.
                 * This is to be able to use the getters and setters from the Sendable_Package class.
                 */
                Sendable_Package data_to_send = new Sendable_Package();
                /**
                 * Takes the string you put in the text fields and saves it as new variables to be added to a Sendable_Package
                 */
                data_to_send.setNick(username_input.getText());

                data_to_send.setIp(ip_input.getText());

                data_to_send.setMessage(text_input.getText());
                /**
                 * Opens a new object output stream and writes the data object we just created to it and writes it
                 */
                ObjectOutputStream data_package = new ObjectOutputStream(sending_socket.getOutputStream());

                data_package.writeObject(data_to_send);

                sending_socket.close();

            } catch (UnknownHostException e1){
                e1.printStackTrace();
            } catch (IOException e1) {
               System.out.println(e1.getMessage());
            }

        }
    }

    /**
     * Creates all the variables used by the method.
     * They are all private, which is an example of encapsulation.
     */
    private JTextField text_input, username_input, ip_input;
    private JTextArea chat_frame;
    private JButton send_Button;

}

/**
 * This creates a package of data, in this case a nickname, ip address and a message.
 * @method getNick(): it calls the actual value of the nick variable.
 * @method getIp(): it calls the actual value of the nick variable.
 * @method getMessage(): it calls the actual value of the nick variable.
 * @method setNick(): updates the value of the nick variable
 * @method setIp(): updates the value of the ip variable
 * @method setMessage(): updates the value of the message variable
 *
 */
class Sendable_Package implements Serializable{
    private String nick,ip,message;

    public String getNick() {
        return nick;
    }

    public String getIp() {
        return ip;
    }

    public String getMessage() {
        return message;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}