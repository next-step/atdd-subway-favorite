package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UsernamePasswordFilter extends AuthenticationFilter {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public UsernamePasswordFilter(LoginService loginService) {
        super(loginService);
    }

    @Override
    protected String getEmail(HttpServletRequest request) {
        String username = request.getParameter(USERNAME);
        if (username == null || username.isBlank()) {
            throw new AuthenticationException();
        }
        return username;
    }

    @Override
    protected String getPassword(HttpServletRequest request) {
        String password = request.getParameter(PASSWORD);
        if (password == null || password.isBlank()) {
            throw new AuthenticationException();
        }
        return request.getParameter(PASSWORD);
    }

    @Override
    protected void execute(HttpServletResponse response, String email, List<String> authorities) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new Authentication(email, authorities));
        response.setStatus(HttpStatus.OK.value());
    }
}
