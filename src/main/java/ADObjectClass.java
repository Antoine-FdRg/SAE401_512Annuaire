import java.util.Objects;

public enum ADObjectClass {
    USER("user", "u", "name"),
    ORGANIZATIONAL_UNIT("organizationalUnit","o", "name"),
    GROUP("group", "g", "name");

    private final String text;
    private final String initial;
    private final String namingAttribute;

    ADObjectClass(String text, String value,String namingAttribute) {
        this.text = text;
        this.initial = value;
        this.namingAttribute = namingAttribute;
    }

    public String toString() {
        return text;
    }

    public String getInitial() {
        return initial;
    }

    public String getNamingAttribute() {
        return namingAttribute;
    }

    public static ADObjectClass fromValue(String value) {
        for (ADObjectClass b : ADObjectClass.values()) {
            if (Objects.equals(b.initial, value)) {
                return b;
            }
        }
        return null;
    }
}
