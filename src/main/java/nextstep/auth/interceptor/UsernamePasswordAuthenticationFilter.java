package nextstep.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainingFilter {

    private static final Logger log = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        super(userDetailsService, null, objectMapper);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameter(EMAIL);
        String credentials = request.getParameter(PASSWORD);
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected void afterAuthentication(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
