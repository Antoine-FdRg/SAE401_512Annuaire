import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        String mdp = "@Arnaudisthebest83";
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Entrez le port :");
//        String port = sc.nextLine();
//        System.out.println("Entrez le nom d'utilisateur (uid) :");
//        String user = sc.nextLine();
//        System.out.println("Entrez le nom de l'ou :");
//        String ou = sc.nextLine();

        Properties env = new Properties();
        env.put(Context.SECURITY_AUTHENTICATION,"Simple");
        env.put(Context.SECURITY_PRINCIPAL, "antoine@EQUIPE1B.local");
        env.put(Context.SECURITY_CREDENTIALS,mdp);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
        try {
            System.out.println("Création de la connexion et authentification...");
            DirContext connection = new InitialDirContext(env);
            System.out.println("Connexion établie !");

            System.out.println("Recherche des utilisateurs...");
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = connection.search("OU=512BankFR,DC=Equipe1B,DC=local", "(objectClass=user)", searchControls);
            System.out.println("Utilisateurs trouvés :");
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                System.out.println(attributes.get("cn"));
            }
            System.out.println("Fin de la recherche");



            connection.close();
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fin du programme");
    }
}
