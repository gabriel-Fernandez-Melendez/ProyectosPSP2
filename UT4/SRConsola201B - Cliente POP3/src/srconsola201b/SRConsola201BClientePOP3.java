package srconsola201b;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

public class SRConsola201BClientePOP3 { //esto puede entrar en el examen

    public static void main(String[] args) {
    	
    	//diferencias entre imap y pop3:
    	/**
    	 * ambos son para recuperar correos de un servidor de correos 
    	 * el dispositivo es el cliente que se conecta la servidor
    	 * POP es el mas antiguo de los dos en 1984, actualmente se usa pop3 
    	 * Imap es del 1986 no solo los recupera si no que tienes acceso remoto a donde esten almacenados estos correos
    	 * por lo cual pop funciona en local(descargando los archivos permanentemente) y imap trabaja con un servidor remoto
    	 * 
    	 * podemos concluir que imap es mas completo y complejo que pop
    	 * 
    	 * pero pop es mas rapido
    	 * 
    	 * imap sustituyo a pop 3 : imap trabaja con una ubicacion en concreto = una ip y por lo  tanto todos los cambios ocurren en el servidor
    	 * 
    	 * pop tenia sentido en la antiguedad por que el uso de internet no era tan comun,no se puede comprobar que hay varios correos iguales en diferentes dispositivos
    	 * 
    	 * ventajas imap : una ventaja es que se accede de forma remota y no local, todos los cambios se sincronizan al momento con el servidor(evita que haya duplicados)
    	 * 
    	 * hoy en dia se recomienda mas el uso de imap 
    	 * 
    	 * 
    	 * 
    	 */
        String server = "localhost", username = "usuario1", password = "usu1";
        int puerto = 110;
        POP3SClient pop3 = new POP3SClient();
        try {
            pop3.connect(server, puerto);
            System.out.println("Conexion realizada al servidor POP3 " + server);
            // nos logueamos
            if (!pop3.login(username, password)) {
                System.err.println("Error al hacer login");
            } else {
                POP3MessageInfo[] men = pop3.listMessages();
                if (men == null) {
                    System.out.println("Imposible recuperar mensajes.");
                } else {
                    System.out.println("Nº de mensajes: " + men.length);
                    if (men.length > 0) {
                        Recuperarmesajes(men, pop3);
                    }
                }
                // finalizar sesion
                pop3.logout();
            }
            // nos desconectamos
            pop3.disconnect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }

    private static void Recuperarmesajes(POP3MessageInfo[] men, POP3SClient pop3)
            throws IOException {
        for (int i = 0; i < men.length; i++) {//
            // POP3MessageInfo msginfo : messages) {
            System.out.println("Mensaje: " + (i + 1));
            POP3MessageInfo msginfo = men[i]; // lista de mensajes
            System.out.println("IDentificador: " + msginfo.identifier
                    + ", Number: " + msginfo.number + ", Tamaño: "
                    + msginfo.size);
            System.out.println("Prueba de listUniqueIdentifier: ");
            POP3MessageInfo pmi = pop3.listUniqueIdentifier(i + 1);// un mensaje
            System.out.println("\tIdentificador: " + pmi.identifier
                    + ", Number: " + pmi.number + ", Tamaño: " + pmi.size);
            // solo recupera cabecera
            System.out.println("Cabecera del mensaje:");
            BufferedReader reader = (BufferedReader) pop3.retrieveMessageTop(msginfo.number, 0);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line.toString());
            }
            reader.close();
            //recupera todo
            BufferedReader reader2 = (BufferedReader) pop3.retrieveMessage(msginfo.number);
            String linea;
            while ((linea = reader2.readLine()) != null) {
                System.out.println(linea.toString());
            }
            reader2.close();
        }
    }
}
