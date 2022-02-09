package nextstep.auth.authentication.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return null;
    }

}