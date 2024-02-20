package nextstep.auth.application;

public interface TokenProvider {
    boolean isSupport(TokenType tokenType);

    String createToken(String principal);

    String getPrincipal(String token);

    boolean validateToken(String token);
}
