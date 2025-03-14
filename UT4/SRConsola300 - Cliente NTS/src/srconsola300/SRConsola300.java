package srconsola300;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

/**
 * Esta clase muestra como comunicarse con un servicio de hora de red NTP.
 * Ejemplo elaborado a partir de la implementacion oficial de ntp.org
 * http://support.ntp.org/bin/view/Support/JavaSntpClient
 *
 * @author Alejandro López Camus
 * @version 1.0
 * @since PSP 1.0
 */
public class SRConsola300 {

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) throws IOException {
        // Dirección del servidor NTP al que consultamos
        String servidorNTP = "0.es.pool.ntp.org"; //SERVIDOR NTP EN ESPAÑA , SE PONE ESTA VARIABLE PARA USARLA COMO EL QUE VIAJARA

        // Preparamos el socket de comunicaciones
        DatagramSocket socket = new DatagramSocket();
        // Decodificamos la dereccion de internet
        InetAddress direccionIP = InetAddress.getByName(servidorNTP);
        // Preparamos contenido del mensaje solicitando la hora
        byte[] buf = new NtpMessage().toByteArray();
        // Preparamos el datagrama
        DatagramPacket paquete
                = new DatagramPacket(buf, buf.length, direccionIP, 123);
        //Enviamos la petición al servidor
        socket.send(paquete);

        System.out.println("Solicitud NTP enviada, esperando respuesta...\n");
        // Recibimos el paquete de respuesta y cerramos el socket
        paquete = new DatagramPacket(buf, buf.length);
        socket.receive(paquete);
        socket.close();

        // Procesamos la respuesta
        NtpMessage mensajeNTP = new NtpMessage(paquete.getData());

        // El mensaje devuelto contiene un registro con varias horas
        // última vez que se sincronizo el reloj del cliente
        System.out.println("Última sincronización del reloj cliente:"
                + ajustarFechaA1970(mensajeNTP.referenceTimestamp));
        //hora del cliente en el momento de realizar la petición
        System.out.println("Hora de la petición del reloj cliente:"
                + ajustarFechaA1970(mensajeNTP.originateTimestamp));
        //hora del servidor en el momento que le llega la petición   
        System.out.println("Hora de llegada del reloj servidor:"
                + ajustarFechaA1970(mensajeNTP.receiveTimestamp));
        //hora del servidor en el momento que nos responde
        System.out.println("Hora de envío del reloj servidor:"
                + ajustarFechaA1970(mensajeNTP.transmitTimestamp));
    }

    /**
     * El servicio NTP trabaja el año 1900 como referencia, java emplea el año
     * 1970 como referencia por lo que debemos ajustar esta diferencia
     * manualmente.
     *
     * @param segundosDesde1900 Segundos transcurridos desde 1900
     * @return Fecha ajustada a las especificaciones de java
     */
    private static Date ajustarFechaA1970(double segundosDesde1900) {
        double segundosDesde1970 = segundosDesde1900 - 2208988800d;
        long milisegundos = (long) (segundosDesde1970 * 1000d);
        return new Date(milisegundos);
    }

}
