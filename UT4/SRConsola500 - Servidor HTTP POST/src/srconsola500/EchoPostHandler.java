package srconsola500;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public class EchoPostHandler implements HttpHandler {

    public EchoPostHandler() {
    }

    @Override

    public void handle(HttpExchange he) {
        // parsear peticion
        Map<String, Object> parametros = new HashMap<String, Object>();
        String respuesta = "";
        try {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            ServidorHTTP.parseQuery(query, parametros);
            // enviar respuesta
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
