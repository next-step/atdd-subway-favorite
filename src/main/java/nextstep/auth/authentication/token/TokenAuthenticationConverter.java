package nextstep.auth.authentication.token;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return null;
    }
}
