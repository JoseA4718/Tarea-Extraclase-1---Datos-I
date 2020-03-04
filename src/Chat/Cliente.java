
package Chat;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}


class MarcoCliente extends JFrame{

    public MarcoCliente(){

        setBounds(0,0,600,430);


        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);
    }

}

class LaminaMarcoCliente extends JPanel implements Runnable{

    public LaminaMarcoCliente(){



        JLabel texto_nombre=new JLabel("Nombre de Usuario:");

        add(texto_nombre);

        nick = new JTextField(10);
        add(nick);

        JLabel texto_ip = new JLabel("       IP del Destinatario:");

        add(texto_ip);


        ip = new JTextField(15);

        add(ip);

        JLabel Texto_chat = new JLabel("\n" + "       CHAT:            ");

        add(Texto_chat);

        campochat = new JTextArea(17,48);

        add(campochat);

        campo1=new JTextField(45);

        add(campo1);

        miboton=new JButton("Enviar");

        EnviaTexto mievento = new EnviaTexto();

        miboton.addActionListener(mievento);

        add(miboton);

        Thread mihilo = new Thread(this);

        mihilo.start();


    }

    @Override
    public void run() {
        try{
            ServerSocket servidor_cliente = new ServerSocket(9090);

            Socket cliente;

            PaqueteEnvio paqueteRecibido;

            while (true){
                cliente = servidor_cliente.accept();


                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();

                campochat.append("\n"+paqueteRecibido.getNick()+": "+ paqueteRecibido.getMensaje());
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private class EnviaTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            campochat.append("\n" + campo1.getText());

            try {
                Socket misocket=new Socket("192.168.0.14", 9999);

                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setNick(nick.getText());

                datos.setIp(ip.getText());

                datos.setMensaje(campo1.getText());

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

    private JTextField campo1, nick, ip;
    private JTextArea campochat;


    private JButton miboton;

}

class PaqueteEnvio implements Serializable{//Hay que serializarlo (convertir a binario el objeto de datos para que pueda viajar por la red en forma de paquetes
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