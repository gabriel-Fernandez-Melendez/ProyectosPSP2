package srconsola200;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * En esta clase se envía un correo electrónico vía SMTPS (SMTP con TLS). La
 * cuenta a través de la que se envía el correo electrónico es necesario que sea
 * configurada para aceptar correos desde este proyecto maestro. En Gmail es
 * necesario configurar la cuenta para que acepte correos desde aplicaciones no
 * seguras.
 *
 * @author Ignacio Fontecha Hernández
 * @version 1.0
 * @since PSP 3.0
 */
public class SRConsola200 {

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) {

        enviarCorreo("uno@gmail.com",
                "password", // Contraseña de la cuenta uno@gmail.com --> https://myaccount.google.com/apppasswords
                "true",
                "true",
                "smtp.gmail.com",
                "587",
                "destino@educastur.org",
                "asunto",
                "contenido2024");
    }

    /**
     * Método que envía un correo electrónico vía SMTP
     *
     * @param usuarioDe Cuenta de correo desde la que se va a enviar el correo
     * @param contrasenaUsuarioDe Contraseña de la cuenta de correo desde la que
     * se va a enviar el correo (https://myaccount.google.com/apppasswords)
     * @param envioSeguroTLS Indicador de que el correo electrónico se envíe de
     * forma segura (utilizando TLS)
     * @param autenticacionUsuarioDe Indicador de que se va a autenticar la
     * cuenta de correo desde el que se va a enviar el correo electrónico
     * @param servidorSMTP Servidor de correo de la cuenta de correo desde la
     * que se va a enviar el correo
     * @param puertoServidorSMTP Puerto del servidor de correo de la cuenta de
     * correo desde la que se va a enviar el correo
     * @param usuarioA Cuenta de correo a la que se va a enviar el correo
     * @param asunto Asunto del correo a enviar
     * @param cuerpo Cuerpo del correo a enviar
     */
    public static void enviarCorreo(String usuarioDe,
            String contrasenaUsuarioDe,
            String envioSeguroTLS,
            String autenticacionUsuarioDe,
            String servidorSMTP,
            String puertoServidorSMTP,
            String usuarioA,
            String asunto,
            String cuerpo) {

        System.out.println("Estableciendo las propiedades ...");
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.starttls.enable", envioSeguroTLS);
        propiedades.put("mail.smtp.auth", autenticacionUsuarioDe);
        propiedades.put("mail.smtp.host", servidorSMTP);
        propiedades.put("mail.smtp.port", puertoServidorSMTP);

        System.out.println("Configurando el autenticador ...");
        Authenticator autenticador = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                ///Hay que usar una password de app:
                /// https://myaccount.google.com/apppasswords
                return new PasswordAuthentication(usuarioDe, contrasenaUsuarioDe);
            }
        };

        System.out.println("Estableciendo una conexión con el servidor SMTP ...");
        Session sesion = Session.getInstance(propiedades, autenticador);

        try {
            System.out.println("Creando el mensaje ...");
            Message mensaje = new MimeMessage(sesion);
            InternetAddress iaDe = new InternetAddress(usuarioDe);
            mensaje.setFrom(iaDe);
            InternetAddress[] iaA = InternetAddress.parse(usuarioA);
            mensaje.setRecipients(Message.RecipientType.TO, iaA);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            System.out.println("Enviando el mensaje ...");
            Transport.send(mensaje);
            System.out.println("Correo electrónico enviado");

        } catch (MessagingException e) {
            System.out.println("Fallo en el envío del correo electrónico. Fallo que se ha producido: " + e.getMessage());
        }
    }

}
