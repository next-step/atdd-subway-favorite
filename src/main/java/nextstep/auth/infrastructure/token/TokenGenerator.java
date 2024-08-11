package nextstep.auth.infrastructure.token;

public interface TokenGenerator {
    String createToken(String principal);
    boolean validateToken(String token);
    String getPrincipal(String token);
}
