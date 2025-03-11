package srconsola500;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class RootHandler implements HttpHandler {

    public RootHandler() {
    }

    @Override
    public void handle(HttpExchange he) {
        try {
            String respuesta = "Servidor activado correctamente en el puerto ";
            he.sendResponseHeaders(200, respuesta.length());
            OutputStream salida = he.getResponseBody();
            salida.write(respuesta.getBytes());
            salida.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

}
