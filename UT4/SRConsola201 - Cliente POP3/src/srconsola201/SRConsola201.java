package srconsola201;

import java.security.Security;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import com.sun.net.ssl.internal.ssl.Provider;

/**
 * En esta clase se leen los correos electrónicos de una determinada cuenta de
 * correo electrónico a través de POP3S (POP3 con TLS). La cuenta de la que se
 * van a leer los correos electrónicos es necesario que sea configurada para
 * aceptar peticiones desde este proyecto maestro. En Gmail es necesario
 * configurar la cuenta para que acepte correos desde aplicaciones no seguras
 *
 * @author Ignacio Fontecha Hernández
 * @version 1.0
 * @since PSP 3.0
 */
public class SRConsola201 {//esto puede entrar en el examen

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) {
    	//esto nos deja conectarnos a gmail con el protocolo pop 
        System.out.println("Estableciendo las propiedades ...");
        Properties propiedades = new Properties();
        String host = "pop.gmail.com";
        String protocolo = "pop3";
        String puerto = "995";
        String usuario = "uno@gmail.com";
        String contrasena = "password"; ///Hay que usar https://myaccount.google.com/apppasswords
        String fabricaSocketsSSL = "javax.net.ssl.SSLSocketFactory";
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        propiedades.setProperty("mail.pop3.socketFactory.class", fabricaSocketsSSL);
        propiedades.setProperty("mail.pop3.socketFactory.fallback", "false");
        propiedades.setProperty("mail.pop3.port", puerto);
        propiedades.setProperty("mail.pop3.socketFactory.port", puerto);
        propiedades.setProperty("mail.pop3.ssl.trust", "*");

        System.out.println("Estableciendo conexión con el servidor POP3 ...");
        Session sesion = Session.getInstance(propiedades, null);
        Store almacen = null;
        Folder bandejaEntrada = null;
        Message[] mensajes = null;
        try {
            ///Hay que usar una password de app:
            /// https://myaccount.google.com/apppasswords
            URLName url = new URLName(protocolo, host, Integer.parseInt(puerto), null, usuario, contrasena);
            almacen = sesion.getStore(url);
        } catch (NoSuchProviderException ex) {
            System.out.println("Fallo: " + ex.getMessage());
        }
        try {
            almacen.connect();
            System.out.println("Leyendo la bandeja de entrada de la cuenta de correo ...");
            //con la orden que esta justo abajo consejimos lo que hay dentro de una carpeta
            bandejaEntrada = almacen.getFolder("Inbox");
            bandejaEntrada.open(Folder.READ_ONLY);
            mensajes = bandejaEntrada.getMessages();
            if (mensajes.length == 0) {
                System.out.println("No hay ningún mensaje");
            }
            int k =0;
            //este bucle acepta dos condicionales
            for (int i = mensajes.length-1; i >= 0; k++, i--) {
                if (k > 5) {
                    System.exit(0);
                    bandejaEntrada.close(true);
                    almacen.close();
                }
                System.out.println("Mensaje " + (i + 1));
                System.out.println("De : " + mensajes[i].getFrom()[0]);
                System.out.println("Asunto: " + mensajes[i].getSubject());
                System.out.println("Fecha de Envío: " + mensajes[i].getSentDate());
                System.out.println();
            }
            bandejaEntrada.close(true);
            almacen.close();
        } catch (MessagingException ex) {
            System.out.println("Fallo: " + ex.getMessage());
        }
    }
}
