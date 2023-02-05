import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static  Scanner sc = new Scanner(System.in);
    private static boolean adminConnected;
    private static String username;

    public static void main(String[] args) {
        String user = getUser();
        String mdp = getMdp();
        System.out.println("Création de la connexion et authentification...");
        DirContext connection = null;
        try {
            connection = connect(user, mdp);
        } catch (NamingException e) {
//            throw new RuntimeException(e);
            System.out.println("Erreur d'authentification");
            System.exit(1);
        }
        run(connection);
        System.out.println("Déconnexion...");
        try {
            connection.close();
        } catch (NamingException e) {
//            throw new RuntimeException(e);
            System.out.println("Erreur de déconnexion");
        }
        System.out.println("Fin du programme");
    }

    private static void run(DirContext connection) {
        while (true) {
            System.out.println("Entrez une commande : ");
            String input = sc.nextLine();
            if (Objects.equals(input, "exit") || Objects.equals(input, "quit")) {
                break;
            }
            switch (input) {
                case "s":
                    search(connection);
                    break;
//            case "a":
//                add(connection);
//                break;
//            case "d":
//                delete(connection);
//                break;
//            case "m":
//                modify(connection);
//                break;
                default:
                    System.out.println("Commande inconnue");
            }
        }
    }

    public static void search(DirContext context){
        System.out.println("Entrez le type d'objet à rechercher : ");
        for (ADObjectClass adObjectClass : ADObjectClass.values()) {
            System.out.println(adObjectClass.getInitial() + " : " + adObjectClass);
        }
        ADObjectClass searchedObject = Objects.requireNonNull(ADObjectClass.fromValue(sc.next()));
        System.out.println("Recherche : ");
        String search = sc.next();
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String filter = "(&(objectClass="+searchedObject+")(name=*"+search+"*))";
        try {
            //todo faire les recherhce de user dans CN=Utilisateurs du domaine,CN=Users,DC=EQUIPE1B,DC=local
            NamingEnumeration<SearchResult> results = context.search("OU=512BankFR,DC=EQUIPE1B,DC=local", filter, searchControls); //connection.search("ou=512Bank,DC=Equipe1B,DC=local", (objectClass=user), searchControls);
            displayResults(results, searchedObject);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }


    }

    private static void displayResults(NamingEnumeration<SearchResult> results, ADObjectClass searchedObject) {
        System.out.println("Résultats :");
        SearchResult searchResult;
        while (true) {
            try {
                if (!results.hasMore()) break;
                searchResult = results.next();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
            Attributes attributes = searchResult.getAttributes();
            System.out.println(attributes.get(searchedObject.getNamingAttribute()));
            if(adminConnected) {
                System.out.println(attributes.get("distinguishedName"));
            }
        }
        System.out.println("\n");
    }

    public static DirContext connect(String user, String mdp) throws NamingException {
        Properties env = new Properties();
        username = user;
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, username+"@EQUIPE1B.local"); //prenom.nom@EQUIPE1B.local
        env.put(Context.SECURITY_CREDENTIALS, mdp);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:3268"); //389
        //  env.put(Context.REFERRAL, "follow");
        DirContext connection = new InitialDirContext(env);
        adminConnected = isAdmin(connection);
        if (adminConnected) {
            System.out.println("Vous êtes connecté en tant qu'administrateur");
        } else {
            System.out.println("Vous êtes connecté en tant qu'utilisateur");
        }
        return connection;
    }

    private static boolean isAdmin(DirContext context) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //todo corriger le filtre pour ne mettre la co a true que si l'utilisateur est dans le groupe Administrateurs (genre pas "antoine")
        String filter = "(&(objectClass=user)(userPrincipalName=*"+username+"*)(memberOf=CN=Administrateurs,CN=Builtin,DC=EQUIPE1B,DC=local))"; //(memberOf=CN=Direction_Secretaire_General,OU=512BankFR,DC=EQUIPE1B,DC=local)
        NamingEnumeration<SearchResult> res;
        try {
            res = context.search("OU=512BankFR,DC=Equipe1B,DC=local", filter, searchControls);
            return res.hasMore();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUser(){
        System.out.println("Entrez votre nom d'utilisateur (sAMAccountName) : ");
        return sc.next();
    }

    public static String getMdp(){
        return "@Arnaudisthebest83";
    }
}
