package srconsola200b;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

public class SRConsola200BClienteSMTP {

    public static void main(String[] args) {

        SMTPClient client = new SMTPClient();
        try {
            int respuesta;
            client.connect("127.0.0.1", 25);
            System.out.print(client.getReplyString());
            respuesta = client.getReplyCode();
            
            if (!SMTPReply.isPositiveCompletion(respuesta)) {
                client.disconnect();
                System.err.println("SMTP server refused connection.");
                System.exit(1);
            }
            
            client.login();
            String remitente = "usuario1@localhost";
            String destino1 = "usuario2@localhost";
            String destino2 = "destino@educastur.org";
            String asunto = "Prueba de SMTPClient";
            String mensaje = "Hola. \nEnviando saludos.\nCiao!";
            //se crea la cabecera
            SimpleSMTPHeader cabecera = new SimpleSMTPHeader(remitente, destino1, asunto);
            cabecera.addCC(destino2);
            //establecer el correo de remitente
            client.setSender(remitente);
            //a�adir correos destino 
            client.addRecipient(destino1);//hay que añadir los dos
            client.addRecipient(destino2);
            //se envia DATA
            Writer writer = client.sendMessageData();
            if (writer == null) { //fallo	       
                System.out.println("FALLO AL ENVIAR DATA.");
                System.exit(1);
            }
            System.out.println(cabecera.toString());
            writer.write(cabecera.toString()); //primero escribo cabecera    
            writer.write(mensaje);//luego mensaje
            writer.close();
            if (!client.completePendingCommand()) { //fallo
                System.out.println("FALLO AL FINALIZAR LA TRANSACCI�N.");
                System.exit(1);
            }
            client.logout();
        } catch (IOException e) {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
