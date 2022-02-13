package nextstep.auth.model.authentication.interceptor;

import nextstep.auth.model.authentication.AuthenticationToken;
import nextstep.auth.model.authentication.UserDetails;
import nextstep.auth.model.authentication.service.UserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

@Component(value = "sessionAuthenticationInterceptor")
public class SessionAuthenticationInterceptor implements AuthenticationInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private UserDetailsService userDetailsService;

    public SessionAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getEmail();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        userDetails.validateCredential(authenticationToken.getPassword());

        return new Authentication(userDetails);
    }

    @Override
    public void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContext.from(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
