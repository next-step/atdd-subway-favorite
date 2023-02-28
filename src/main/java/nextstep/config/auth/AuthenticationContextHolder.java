package nextstep.config.auth;

public class AuthenticationContextHolder {
    private static final ThreadLocal<String> authenticationHolder = new ThreadLocal<>();

    public static void clearContext() {
        authenticationHolder.remove();
    }

    public static String getAuthentication() {
        return authenticationHolder.get();
    }

    public static void setAuthentication(String principal) {
        authenticationHolder.set(principal);
    }

}
