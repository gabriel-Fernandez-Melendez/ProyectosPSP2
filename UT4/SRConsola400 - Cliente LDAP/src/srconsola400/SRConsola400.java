package srconsola400;

import java.util.Map;
import java.util.Scanner;
import javax.naming.NamingException;

/**
 * En esta clase se muestra cómo llevar a cabo la autenticación contra un
 * servidor LDAP
 *
 * @author Ignacio Fontecha Hernández
 * @version 1.0
 * @since PSP 3.0
 */
public class SRConsola400 {

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) {
        Autenticador a = new Autenticador("iesataulfo.local", "ldap://172.16.1.11", "dc=iesataulfo,dc=local");

        Scanner teclado = new Scanner(System.in);

        System.out.println("Introduce el usuario: ");
        String usuario = teclado.nextLine();

        System.out.println("Introduce la contraseña: ");
        String contrasena = teclado.nextLine();

        try {
            String[] atributosADevolver = {"sn", "givenName", "mail", "description", "title"};
            Map atributosDevueltos = a.autenticar(usuario, contrasena, atributosADevolver);
            System.out.println("Acceso autorizado !!!");
            System.out.println("Atributo sn: " + atributosDevueltos.get("givenName"));
            System.out.println("Atributo givenName: " + atributosDevueltos.get("sn"));
            System.out.println("Atributo mail: " + atributosDevueltos.get("mail"));
            System.out.println("Atributo description: " + atributosDevueltos.get("description"));
            System.out.println("Atributo title: " + atributosDevueltos.get("title"));
        } catch (NamingException ex) {
            System.out.println("Acceso NO autorizado !!!");
        }
    }

}
