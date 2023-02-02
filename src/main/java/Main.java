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
            DirContext connection = new InitialDirContext(env);
            System.out.println("Connection ="+ connection);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
