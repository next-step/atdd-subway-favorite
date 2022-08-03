package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UsernamePasswordAuthenticationFilter implements AuthenticationFilterStrategy {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        return new Authentication(
            isNotNullAndNotEmpty(request.getParameter(USERNAME)),
            isNotNullAndNotEmpty(request.getParameter(PASSWORD))
        );
    }

    @Override
    public void responseOk(HttpServletResponse response, String email, List<String> authorities) {
        SecurityContextHolder.getContext()
            .setAuthentication(new Authentication(email, authorities));
        response.setStatus(HttpStatus.OK.value());
    }

    private String isNotNullAndNotEmpty(String parameter) {
        if (parameter == null || parameter.isBlank()) {
            throw new AuthenticationException();
        }
        return parameter;
    }
}
