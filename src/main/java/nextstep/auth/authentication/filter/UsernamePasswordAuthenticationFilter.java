package nextstep.auth.authentication.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationFilter {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected Authentication convert(HttpServletRequest request) throws IOException {
        String username = request.getParameter(USERNAME_FIELD);
        String password = request.getParameter(PASSWORD_FIELD);

        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
