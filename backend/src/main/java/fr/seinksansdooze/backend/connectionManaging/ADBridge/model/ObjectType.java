package fr.seinksansdooze.backend.connectionManaging.ADBridge.model;


public enum ObjectType {
    PERSON("user",  "name"),
    STRUCTURE("organizationalUnit",  "name"),
    GROUP("group",  "name");

    private final String text;
    private final String namingAttribute;

    ObjectType(String text, String namingAttribute) {
        this.text = text;
        this.namingAttribute = namingAttribute;
    }

    public String toString() {
        return text;
    }

    public String getNamingAttribute() {
        return namingAttribute;
    }

}
