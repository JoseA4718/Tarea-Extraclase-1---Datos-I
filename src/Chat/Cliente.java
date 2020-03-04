
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

class ClientFrame_Canvas extends JPanel implements Runnable{

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


        EnviaTexto mievento = new EnviaTexto();

        send_Button.addActionListener(mievento);
        add(send_Button);

        Thread mihilo = new Thread(this);
        mihilo.start();


    }

    @Override
    public void run() {
        try{
            ServerSocket servidor_cliente = new ServerSocket(9090);

            Socket cliente;

            Sendable_Package paqueteRecibido;

            while (true){
                cliente = servidor_cliente.accept();


                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido = (Sendable_Package) flujoentrada.readObject();

                chat_frame.append("\n"+paqueteRecibido.getNick()+": "+ paqueteRecibido.getMensaje());
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private class EnviaTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            chat_frame.append("\n" + text_input.getText());

            try {
                Socket misocket=new Socket("192.168.0.14", 9999);

                Sendable_Package datos = new Sendable_Package();

                datos.setNick(username_input.getText());

                datos.setIp(ip_input.getText());

                datos.setMensaje(text_input.getText());

                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());

                paquete_datos.writeObject(datos);

                misocket.close();

                /*DataOutputStream flujo_salida = new DataOutputStream(misocket.getOutputStream());

                flujo_salida.writeUTF(campo1.getText());

                flujo_salida.close();*/


            } catch (UnknownHostException e1){
                e1.printStackTrace();
            } catch (IOException e1) {
               System.out.println(e1.getMessage());
            }

        }
    }

    private JTextField text_input, username_input, ip_input;
    private JTextArea chat_frame;


    private JButton send_Button;

}

class Sendable_Package implements Serializable{//Hay que serializarlo (convertir a binario el objeto de datos para que pueda viajar por la red en forma de paquetes
    private String nick,ip,mensaje;

    public String getNick() {
        return nick;
    }

    public String getIp() {
        return ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}