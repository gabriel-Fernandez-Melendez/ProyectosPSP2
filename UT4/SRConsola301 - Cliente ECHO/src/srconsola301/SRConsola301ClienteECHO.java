package srconsola301;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class SRConsola301ClienteECHO {

    public static void main(String[] args) { //FUNCIONA SOLO PARA ENVIAR UNA PETICION
        Socket socket;
        PrintWriter fsalida;
        BufferedReader fentrada;
        BufferedReader in;
        try {
            socket = new Socket("localhost", 4344);
            fsalida = new PrintWriter(socket.getOutputStream(), true);
            fentrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            in = new BufferedReader(new InputStreamReader(System.in));
            String cadena, eco = "";
            System.out.println("Introduce cadena: ");
            cadena = in.readLine();
            while (cadena != null) {
                fsalida.println(cadena);
                eco = fentrada.readLine();
                System.out.println("ECO: [" + eco + "]");
                System.out.println("Introduce cadena: ");
                cadena = in.readLine();
            }
            fsalida.close();
            fentrada.close();
            System.out.println("Fin del envio.");
            in.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("IOException: "+ex);
        } 
    }

}
