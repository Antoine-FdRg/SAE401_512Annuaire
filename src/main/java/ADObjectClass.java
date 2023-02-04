public enum ADObjectClass {
    USER("user", 1, "name"),
    ORGANIZATIONAL_UNIT("organizationalUnit",2, "name"),
    ;

    private final String text;
    private final int value;
    private final String namingAttribute;

    ADObjectClass(String text, int value,String namingAttribute) {
        this.text = text;
        this.value = value;
        this.namingAttribute = namingAttribute;
    }

    public String toString() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public String getNamingAttribute() {
        return namingAttribute;
    }

    public static ADObjectClass fromValue(int value) {
        for (ADObjectClass b : ADObjectClass.values()) {
            if (b.value == value) {
                return b;
            }
        }
        return null;
    }
}
