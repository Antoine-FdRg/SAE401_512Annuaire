import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String user = "uid=arnaud,ou=system";
        String mdp = "@Arnaudisthebest83";

        Scanner sc = new Scanner(System.in);
        System.out.println("entrer le port :");
        String port = sc.nextLine();

        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost"+port);
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS,mdp);
        try {
            System.out.println("Création de la connexion");
            DirContext connection = new InitialDirContext(env);
            System.out.println("Connexion établie");
            System.out.println("Connexion " + connection);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fin du programme");
    }
}
