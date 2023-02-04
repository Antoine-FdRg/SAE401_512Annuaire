import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NamingException {

        String mdp = "@Arnaudisthebest83";
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Entrez le port :");
//        String port = sc.nextLine();
//        System.out.println("Entrez le nom d'utilisateur (uid) :");
//        String user = sc.nextLine();
//        System.out.println("Entrez le nom de l'ou :");
//        String ou = sc.nextLine();

        Properties env = new Properties();
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, "antoine@EQUIPE1B.local");
        env.put(Context.SECURITY_CREDENTIALS, mdp);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
        try {
            System.out.println("Création de la connexion et authentification...");
            DirContext connection = new InitialDirContext(env);
            System.out.println("Connexion établie !");

            System.out.println("Recherche des utilisateurs...");
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            Scanner sc = new Scanner(System.in);
            do {
                System.out.println("Entrez une requete de recherche d'utilisateur : ");
                String input = sc.nextLine();
                if (Objects.equals(input, "exit") || Objects.equals(input, "quit")) {
                    break;
                }
                System.out.println("Entrez le type d'objet à rechercher : ");
                for (ADObjectClass adObjectClass : ADObjectClass.values()) {
                    System.out.println(adObjectClass.getValue() + " : " + adObjectClass);
                }
                ADObjectClass objectClass = Objects.requireNonNull(ADObjectClass.fromValue(sc.nextInt()));
                String filter = "(&(objectClass="+objectClass+")(name=*"+input+"*))";
                NamingEnumeration<SearchResult> results = connection.search("ou=512BankFR,DC=Equipe1B,DC=local", filter, searchControls); //connection.search("ou=512Bank,DC=Equipe1B,DC=local", (objectClass=user), searchControls);
                System.out.println("Résultats :");
                while (results.hasMore()) {
                    SearchResult searchResult = results.next();
                    Attributes attributes = searchResult.getAttributes();
                    System.out.println(attributes.get(objectClass.getNamingAttribute()));
                    System.out.println("\n");
                }
                System.out.println("Fin de la recherche");
            } while (true);
            System.out.println("Déconnexion...");
            connection.close();
        } catch (Exception e) {
            throw new NamingException(e.getMessage());
        }
        System.out.println("Fin du programme");
    }
}
