package nextstep.auth.authentication.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;

public class UsernamePasswordAuthenticationFilter extends AbstractOneAuthenticationFilter {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(authenticationManager, authenticationSuccessHandler);
    }

    @Override
    protected Authentication convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter(USERNAME_FIELD);
        String password = request.getParameter(PASSWORD_FIELD);

        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
