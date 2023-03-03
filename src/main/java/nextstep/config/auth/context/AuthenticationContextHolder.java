package nextstep.config.auth.context;

import java.util.Objects;

public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> authenticationContextHolder;

    static {
        authenticationContextHolder = new ThreadLocal<>();
    }

    public static void clearContext() {
        authenticationContextHolder.remove();
    }

    public static Authentication getContext() {
        Authentication authentication = authenticationContextHolder.get();

        if (Objects.isNull(authentication)) {
            authentication = createEmptyAuthenticationContext();
            authenticationContextHolder.set(authentication);
        }

        return authentication;
    }

    public static void setContext(Authentication authentication) {
        if (Objects.nonNull(authentication)) {
            authenticationContextHolder.set(authentication);
        }
    }

    private static Authentication createEmptyAuthenticationContext() {
        return new Authentication();
    }
}
