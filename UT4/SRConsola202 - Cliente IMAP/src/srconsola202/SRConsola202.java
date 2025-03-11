package srconsola202;

import java.security.Security;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 * En esta clase se leen los correos electrónicos de una determinada cuenta de
 * correo electrónico a través de IMAPS (IMAP con TLS). La cuenta de la que se
 * van a leer los correos electrónicos es necesario que sea configurada para
 * aceptar peticiones desde este proyecto maestro. En Gmail es necesario
 * configurar la cuenta para que acepte correos desde aplicaciones no seguras
 *
 * @author Ignacio Fontecha Hernández
 * @version 1.0
 * @since PSP 3.0
 */
public class SRConsola202 {//esto puede entrar en el examen

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) {
        System.out.println("Estableciendo las propiedades ...");
        Properties propiedades = new Properties();
        String host = "imap.gmail.com";
        String protocolo = "imap";
        String puerto = "993";
        String usuario = "uno@gmail.com";
        String contrasena = "password"; /// https://myaccount.google.com/apppasswords
        String fabricaSocketsSSL = "javax.net.ssl.SSLSocketFactory";
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        propiedades.setProperty("mail.imap.socketFactory.class", fabricaSocketsSSL);
        propiedades.setProperty("mail.imap.socketFactory.fallback", "false");
        propiedades.setProperty("mail.imap.port", puerto);
        propiedades.setProperty("mail.imap.socketFactory.port", puerto);
        propiedades.setProperty("mail.imap.ssl.trust", "*");

        System.out.println("Estableciendo conexión con el servidor IMAP ...");
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
            bandejaEntrada = almacen.getFolder("Inbox");
            bandejaEntrada.open(Folder.READ_ONLY);
            mensajes = bandejaEntrada.getMessages();
            if (mensajes.length == 0) {
                System.out.println("No hay ningún mensaje");
            }
            for (int i = 0; i < mensajes.length; i++) {
                if (i > 5) {
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
