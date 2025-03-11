/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srconsola301;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author luis
 */
public class SRConsola301ServidorECHO {

    public static void main(String[] args) {
        ServerSocket servidor;
        String cadena;
        PrintWriter fsalida;
        BufferedReader fentrada;
        try {
            servidor = new ServerSocket(4344);
            System.out.println("Esperando conexiones...: ");
            Socket socket = servidor.accept();
            System.out.println("Cliente conectado!");
            fsalida = new PrintWriter(socket.getOutputStream(), true);
            fentrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((cadena = fentrada.readLine()) != null) {
                fsalida.println(cadena);
                System.out.println("Recibiendo la cadena: ");
                if (cadena.equals("@")) {
                    break;
                }
            }
            fsalida.close();
            fentrada.close();
            servidor.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("IOException: " + ex);
        }
    }
}
