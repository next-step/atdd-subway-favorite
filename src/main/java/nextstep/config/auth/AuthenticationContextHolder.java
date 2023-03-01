package nextstep.config.auth;

import java.util.Objects;

public class AuthenticationContextHolder {
    private static final ThreadLocal<String> authenticationHolder = new ThreadLocal<>();

    public static void clearContext() {
        authenticationHolder.remove();
    }

    public static String getAuthentication(String principal) {
        String accessToken = authenticationHolder.get();
        if (Objects.isNull(accessToken)) {
            authenticationHolder.set(principal);
            accessToken = principal;
        }
        return accessToken;
    }

    public static void setAuthentication(String principal) {
        authenticationHolder.set(principal);
    }

}
