import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static Scanner sc = new Scanner(System.in);
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
            System.out.println("Erreur d'authentification");
            System.exit(1);
        }
        run(connection);
        System.out.println("Déconnexion...");
        try {
            connection.close();
        } catch (NamingException e) {
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
                case "s" -> search(connection);
                case "c" -> create(connection);
                case "d" -> delete(connection);
                case "a" -> addUserToGroup(connection);
                default -> System.out.println("Commande inconnue");
            }
        }
    }

    private static void addUserToGroup(DirContext connection) {
        System.out.println("Entrez le nom de l'utilisateur : ");
        String user = sc.next();
        System.out.println("Entrez le nom du groupe : ");
        String group = sc.next();
        try {
            Attributes attributes = connection.getAttributes("CN=" + user + ",OU=512BankFR,DC=EQUIPE1B,DC=local");
            Attribute memberOf = attributes.get("memberOf");
            if (memberOf == null) {
                memberOf = new BasicAttribute("memberOf");
            }
            memberOf.add("CN=" + group + ",OU=512BankFR,DC=EQUIPE1B,DC=local");
            //todo : corriger le bug qui empêche l'ajout de l'utilisateur au groupe
            //todo : vérifier que l'utilisateur n'est pas déjà dans le groupe
            connection.modifyAttributes("CN=" + user + ",OU=512BankFR,DC=EQUIPE1B,DC=local", DirContext.REPLACE_ATTRIBUTE, attributes);
            System.out.println("Utilisateur ajouté au groupe");
        } catch (NamingException e) {
//            throw new RuntimeException(e);
            System.out.println("Erreur lors de l'ajout de l'utilisateur au groupe");
        }

    }

    private static void delete(DirContext connection) {
        if (!adminConnected) {
            System.out.println("Entrez le type d'objet à supprimer : ");
            for (ADObjectClass adObjectClass : ADObjectClass.values()) {
                System.out.println(adObjectClass.getInitial() + " : " + adObjectClass);
            }
//            ADObjectClass deletedObject = Objects.requireNonNull(ADObjectClass.fromValue(sc.next()));
            System.out.println("Entrez le nom de l'objet : ");
            String name = sc.next();
            try {
                connection.destroySubcontext("CN=" + name + ",OU=512BankFR,DC=EQUIPE1B,DC=local");
                System.out.println("Objet supprimé avec succès");
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Vous n'avez pas les droits pour supprimer un objet");
        }
    }

    private static void create(DirContext connection) {
        System.out.println("Entrez le type d'objet à créer : ");
        for (ADObjectClass adObjectClass : ADObjectClass.values()) {
            System.out.println(adObjectClass.getInitial() + " : " + adObjectClass);
        }
        ADObjectClass createdObject = Objects.requireNonNull(ADObjectClass.fromValue(sc.next()));
        System.out.println("Entrez le nom de l'objet : ");
        String name = sc.next();
        Attributes attributes = new BasicAttributes();

        Attribute objectClass = new BasicAttribute("objectClass");
        objectClass.add(createdObject.toString());
        attributes.put(objectClass);

        Attribute namingAttribute = new BasicAttribute(createdObject.getNamingAttribute());
        namingAttribute.add(name);
        attributes.put(namingAttribute);

        try {
            connection.createSubcontext("CN=" + name + ",OU=512BankFR,DC=EQUIPE1B,DC=local", attributes);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

    }

    public static void search(DirContext context) {
        System.out.println("Entrez le type d'objet à rechercher : ");
        for (ADObjectClass adObjectClass : ADObjectClass.values()) {
            System.out.println(adObjectClass.getInitial() + " : " + adObjectClass);
        }
        ADObjectClass searchedObject = Objects.requireNonNull(ADObjectClass.fromValue(sc.next()));
        System.out.println("Recherche : ");
        String search = sc.next();
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String filter = "(&(objectClass=" + searchedObject + ")(name=*" + search + "*))";
        try {
            NamingEnumeration<SearchResult> results = context.search("OU=512BankFR,DC=EQUIPE1B,DC=local", filter, searchControls); //context.search("ou=512Bank,DC=Equipe1B,DC=local", (objectClass=user), searchControls);
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
            if (adminConnected) {
                System.out.println(attributes.get("distinguishedName"));
            }
        }
        System.out.println("\n");
    }

    public static DirContext connect(String user, String mdp) throws NamingException {
        Properties env = new Properties();
        username = user + "@EQUIPE1B.local";
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, username); //prenom.nom@EQUIPE1B.local
        env.put(Context.SECURITY_CREDENTIALS, mdp);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.22.32.2:389"); //389 (search et createSubcontext) ou 3268 (search only)
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
        String filter = "(&(objectClass=user)(userPrincipalName=" + username + ")(memberOf=CN=Administrateurs,CN=Builtin,DC=EQUIPE1B,DC=local))";
        NamingEnumeration<SearchResult> res;
        try {
            res = context.search("OU=512BankFR,DC=Equipe1B,DC=local", filter, searchControls);
            return res.hasMore();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUser() {
        System.out.println("Entrez votre nom d'utilisateur (sAMAccountName) : ");
        return sc.next();
    }

    public static String getMdp() {
        return "@Arnaudisthebest83";
    }
}
