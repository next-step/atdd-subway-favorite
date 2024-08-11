package nextstep.auth.application;

public interface TokenProvider {
    String createToken(String principal);
    String getPrincipal(String token);
    boolean validateToken(String token);
}

