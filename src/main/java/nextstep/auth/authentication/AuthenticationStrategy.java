package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationStrategy {

    void authenticate(HttpServletRequest request);
}
