package nextstep.auth.filter;

import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationStrategy {
    String getToken(HttpServletRequest request);

    boolean validToken(String token);

    Authentication getAuthentication(String token);

    Authentication getAuthentication(Authentication authentication);

    boolean validUser(Authentication authentication);
}
