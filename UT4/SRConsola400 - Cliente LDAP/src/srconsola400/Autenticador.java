package srconsola400;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * En esta clase se incluye la funcionalidad que permite llevar a acabo la
 * autenticación contra un servidor LDAP, así ´omo la obtención de algunos de
 * sus atributos LDAP
 *
 * @author Ignacio Fontecha Hernández
 * @version 1.0
 * @since PSP 3.0
 */
public class Autenticador {

    private final String dominio;
    private final String URLServidorLDAP;
    private final String nombreDominio;

    /**
     * Método constructor de la clase
     *
     * @param dominio Dominio LDAP
     * @param URLServidorLDAP URL del servidor LDAP
     * @param nombreDominio Nombre del dominio
     */
    public Autenticador(String dominio, String URLServidorLDAP, String nombreDominio) {
        this.dominio = dominio;
        this.URLServidorLDAP = URLServidorLDAP;
        this.nombreDominio = nombreDominio;
    }

    /**
     * Método que autentica un determinado usuario ante el servidor LDAP, y
     * obtiene de dicho servidor una serie de atributos LDAP de dicho usuario
     *
     * @param usuario Usuario a autenticar
     * @param contrasena Contraseña del usuario
     * @param atributosADevolver Atributos LDAP a devolver
     * @return Lista de atributos LDAP del usuario autenticado
     * @throws javax.naming.NamingException En el caso de que la autenticación
     * sea fallida
     */
    public Map autenticar(String usuario, String contrasena, String atributosADevolver[]) throws NamingException {
        System.out.println("Estableciendo los controles de búsqueda ...");
        SearchControls escenarioControlesBusqueda = new SearchControls();
        escenarioControlesBusqueda.setReturningAttributes(atributosADevolver);
        escenarioControlesBusqueda.setSearchScope(SearchControls.SUBTREE_SCOPE);

        System.out.println("Estableciendo el contexto LDAP con las propiedades del entorno ...");
        Hashtable propiedadesEntorno = new Hashtable();
        propiedadesEntorno.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        propiedadesEntorno.put(Context.PROVIDER_URL, URLServidorLDAP);
        propiedadesEntorno.put(Context.SECURITY_AUTHENTICATION, "simple");
        propiedadesEntorno.put(Context.SECURITY_PRINCIPAL, usuario + "@" + dominio);
        propiedadesEntorno.put(Context.SECURITY_CREDENTIALS, contrasena);
        LdapContext contextoLDAP = new InitialLdapContext(propiedadesEntorno, null);

        System.out.println("Estableciendo el filtro de búsqueda y realizando la petición al servidor LDAP ...");
        String filtroBusqueda = "(&(objectClass=user)(sAMAccountName=" + usuario + "))";
        NamingEnumeration respuestaLDAP = contextoLDAP.search(nombreDominio, filtroBusqueda, escenarioControlesBusqueda);

        System.out.println("Extrayendo los resultados de la respuesta del servidor LDAP ...");
        while (respuestaLDAP.hasMoreElements()) {
            SearchResult resultadoBusqueda = (SearchResult) respuestaLDAP.next();
            Attributes atributos = resultadoBusqueda.getAttributes();
            Map listaAtributos = null;
            if (atributos != null) {
                listaAtributos = new HashMap();
                NamingEnumeration listaAtributosConNombre = atributos.getAll();
                while (listaAtributosConNombre.hasMore()) {
                    Attribute atributo = (Attribute) listaAtributosConNombre.next();
                    listaAtributos.put(atributo.getID(), atributo.get());
                }
                listaAtributosConNombre.close();
            }
            return listaAtributos;
        }
        return null;
    }
}
