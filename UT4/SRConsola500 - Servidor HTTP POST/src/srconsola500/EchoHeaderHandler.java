package srconsola500;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author luis
 */
public class EchoHeaderHandler implements HttpHandler {

    public EchoHeaderHandler() {
    }

    @Override
    public void handle(HttpExchange he) {
        Headers cabeceras = he.getRequestHeaders();
        Set<Map.Entry<String, List<String>>> entries = cabeceras.entrySet();
        String respuesta = "";
        for (Map.Entry<String, List<String>> entry : entries) {
            respuesta += entry.toString() + "\n";
        }
        try {
            he.sendResponseHeaders(200, respuesta.length());
            OutputStream salida = he.getResponseBody();
            salida.write(respuesta.getBytes());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

}
