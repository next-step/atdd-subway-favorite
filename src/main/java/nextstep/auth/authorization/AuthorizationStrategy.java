package nextstep.auth.authorization;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationStrategy {

    void authorize(HttpServletRequest request);
}
