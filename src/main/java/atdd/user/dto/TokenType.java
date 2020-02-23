package atdd.user.dto;

public enum TokenType {
    BEARER("Bearer");

    private final String typeName;

    TokenType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
