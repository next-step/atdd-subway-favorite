package nextstep.auth.filter;

import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationStrategy {
    String getToken(HttpServletRequest request);

    Authentication getAuthentication(String token);

    Authentication getAuthentication(Authentication user);

    boolean validUser(Authentication authentication);

    void saveSecurityContext(Authentication authentication);
}
