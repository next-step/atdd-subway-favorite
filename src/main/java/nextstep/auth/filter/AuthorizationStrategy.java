package nextstep.auth.filter;

import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationStrategy {
    String getToken(HttpServletRequest request);
    Authentication getAuthentication(String token);
    Authentication extractAuthentication(String token);
}
