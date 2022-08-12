package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class AbstractCreateAuthenticationFilter implements HandlerInterceptor {

    protected UserDetailsService userDetailsService;

    public AbstractCreateAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request) throws IOException;

    protected abstract String returnAuthenticationToken(String principal, List<String> authorities) throws JsonProcessingException;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        AuthenticationToken authenticationToken = getAuthenticationToken(request);

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        if(userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        String responseToClient = returnAuthenticationToken(userDetails.getEmail(), userDetails.getAuthorities());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
        return false;
    }

}
