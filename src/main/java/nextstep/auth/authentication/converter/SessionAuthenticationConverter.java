package nextstep.auth.authentication.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return null;
    }

}
