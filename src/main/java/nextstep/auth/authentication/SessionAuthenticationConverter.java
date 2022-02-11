package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return null;
    }

}
