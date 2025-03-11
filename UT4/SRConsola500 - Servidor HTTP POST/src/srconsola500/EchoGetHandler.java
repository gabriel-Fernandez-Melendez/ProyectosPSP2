package srconsola500;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class EchoGetHandler implements HttpHandler {

    public EchoGetHandler() {
    }

    @Override
    public void handle(HttpExchange he) {
        // parsear peticion
        Map<String, Object> parametros = new HashMap<String, Object>();
        URI uri = he.getRequestURI();
        String query = uri.getRawQuery();
        String respuesta = "";
        try {
            ServidorHTTP.parseQuery(query, parametros);
            //enviar respuesta
            for (String key : parametros.keySet()) {
                respuesta += key + " = " + parametros.get(key) + "n";
            }
            he.sendResponseHeaders(200, respuesta.length());
            OutputStream salida = he.getResponseBody();
            salida.write(respuesta.getBytes());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

}
