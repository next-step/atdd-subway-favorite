package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationStrategy {

    void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
