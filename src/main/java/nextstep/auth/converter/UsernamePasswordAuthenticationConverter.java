package nextstep.auth.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class UsernamePasswordAuthenticationConverter implements AuthenticationConverter {
    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        return new AuthenticationToken(username, password);
    }
}
