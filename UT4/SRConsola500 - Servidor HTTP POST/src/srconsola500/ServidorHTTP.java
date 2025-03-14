package srconsola500;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public class ServidorHTTP {

    public static void main(String[] args) { // esto pueede entrar tambien en el examen 
        HttpServer servidor;
        try {
            servidor = HttpServer.create(new InetSocketAddress(5500), 0);
            System.out.println("Servidor HTTP creado.");
            servidor.createContext("/", new RootHandler());
            servidor.createContext("/echoHeader", new EchoHeaderHandler());
            servidor.createContext("/echoGet", new EchoGetHandler());
            servidor.createContext("/echoPost", new EchoPostHandler());
            servidor.setExecutor(null);
            servidor.start();
        } catch (IOException ex) {
            System.err.println("Error e el servidor HTTP: " + ex);
        }
    }

    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }
                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List values = (List) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List values = new ArrayList();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

}
