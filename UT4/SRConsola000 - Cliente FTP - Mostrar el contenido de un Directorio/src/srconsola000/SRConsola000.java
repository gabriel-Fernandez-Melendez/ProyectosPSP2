package srconsola000;

import java.io.IOException;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * En esta clase se establece una conexión de cliente a un servidor FTP, se hace
 * login, se cambia el directorio activo, se muestra su contenido, se hace
 * logout y se cierra la conexión con el servidor
 *
 * @author Ignacio Fontecha Hernández
 * @version 1.0
 * @since PSP 1.0
 */
public class SRConsola000 {

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) {
        try {
            System.out.println("Estableciendo conexión con el Servidor FTP");
            FTPClient clienteFTP = new FTPClient();
            String servidorFTP = "ftp.rediris.es";
            clienteFTP.connect(servidorFTP);
            mostrarRespuesta(clienteFTP);

            System.out.println("Autenticándose ante el Servidor FTP");
            String usuario = "anonymous";
            String contrasena = "anonymous";
            boolean login = clienteFTP.login(usuario, contrasena);
            mostrarRespuesta(clienteFTP);

            System.out.println("Cambiando al directorio /debian ");
            clienteFTP.changeWorkingDirectory("/debian");
            mostrarRespuesta(clienteFTP);

            System.out.println("Mostrando el contendio del directorio " + clienteFTP.printWorkingDirectory());
            mostrarRespuesta(clienteFTP);
            FTPFile[] listaFicheros = clienteFTP.listFiles();
            int cont = 0;
            while (cont < listaFicheros.length) {
                mostrarFichero(listaFicheros[cont]);
                cont++;
            }

            System.out.println("Haciendo logout del Servidor FTP");
            boolean logout = clienteFTP.logout();
            mostrarRespuesta(clienteFTP);

            System.out.println("Desconectando del Servidor FTP");
            clienteFTP.disconnect();
            mostrarRespuesta(clienteFTP);
        } catch (IOException ex) {
            System.out.println("Excepción: " + ex);
        }

    }

    /**
     * Método que convierte un String en formato ISO-8859-1 a formato UTF-8
     *
     * @param mensaje String en formato ISO-8859-1
     * @return String en formato UTF-8
     */
    public static String convertirUTF8(String mensaje) {
        if (mensaje == null) {
            return null;
        } else {
            byte[] apoyo = mensaje.getBytes(ISO_8859_1);
            return (new String(apoyo, UTF_8));
        }
    }

    /**
     * Método que muestra la respuesta enviada por el servidor FTP al cliente
     *
     * @param clienteFTP String en formato ISO-8859-1
     */
    public static void mostrarRespuesta(FTPClient clienteFTP) {
        String mensajeRespuesta = convertirUTF8(clienteFTP.getReplyString());
        System.out.println("Respuesta:\n" + mensajeRespuesta);
        int codigoRespuesta = clienteFTP.getReplyCode();
        if (!FTPReply.isPositiveCompletion(codigoRespuesta)) {
            System.out.println("ERROR de Conexión - Código de Error: " + codigoRespuesta);
        }
    }

    /**
     * Método que muestra algunos de los datos de un objeto FTPFile
     *
     * @param fichero Fichero del que obtener los datos
     */
    public static void mostrarFichero(FTPFile fichero) {
        String tipos[] = {"Fichero", "Directorio", "Enlace Simbólico"};
        System.out.printf("%30s %20d %25s\n", fichero.getName(), fichero.getSize(), tipos[fichero.getType()]);
    }

}
