package srconsola200b;

import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import org.apache.commons.net.smtp.*;

public class SRConsola200BClienteSMTPAuth {

    public static void main(String[] args) throws NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyStoreException, InvalidKeyException,
            InvalidKeySpecException {

        AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();

        String server = "127.0.0.1";
        String username = "usuario2";
        String password = "usu2";
        String p = "25";
        int puerto = Integer.parseInt(p);
        try {
            int respuesta;
            // Creacion de la clave para establecer un canal seguro
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(null, null);
            KeyManager km = kmf.getKeyManagers()[0];
            // nos conectamos
            client.connect(server, puerto);
            System.out.println("1 - " + client.getReplyString());
            // se establece la clave
            client.setKeyManager(km);
            respuesta = client.getReplyCode();
            if (!SMTPReply.isPositiveCompletion(respuesta)) {
                client.disconnect();
                System.err.println("SMTP server refused connection.");
                System.exit(1);
            }
            client.ehlo(server);// necesario
            System.out.println("2 - " + client.getReplyString());

            System.out.println("3 - " + client.getReplyString());
            if (client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, username, password)) {
                System.out.println("4 - " + client.getReplyString());
                String destino1 = "usuario1@localhost";
                String destino2 = "destino@educastur.org";
                String asunto = "Prueba de SMTPClient con argo.";
                String mensaje = "Hola. \nEnviando saludossssss...\nCiao!";
                // si el nombre de usuario no es igual al email del usuario
                // definir String origen="email del usuario";
                String origen = username;
                // se crea la cabecera
                SimpleSMTPHeader cabecera = new SimpleSMTPHeader(origen, destino1, asunto);
                client.setSender(origen);
                client.addRecipient(destino1);
                client.addRecipient(destino2);
                System.out.println("5 - " + client.getReplyString());
                // se envia DATA
                Writer writer = client.sendMessageData();
                if (writer == null) { // fallo
                    System.out.println("FALLO AL ENVIAR DATA.");
                    System.exit(1);
                }
                writer.write(cabecera.toString());
                writer.write(mensaje);
                writer.close();
                System.out.println("6 - " + client.getReplyString());
                boolean exito = client.completePendingCommand();
                System.out.println("7 - " + client.getReplyString());
                if (!exito) { 
                    System.out.println("FALLO AL FINALIZAR LA TRANSACCION.");
                    System.exit(1);
                }
            } else {
                System.out.println("USUARIO NO AUTENTICADO.");
            }
        } catch (IOException e) {
            System.err.println("Could not connect to server." + e.getMessage());
            System.exit(1);
        }
        try {
            client.disconnect();
        } catch (IOException ex) {
            System.err.println("Could not connect to server." + ex.getMessage());
        }
        System.out.println("Fin de envio.");
        System.exit(0);
    }
}
