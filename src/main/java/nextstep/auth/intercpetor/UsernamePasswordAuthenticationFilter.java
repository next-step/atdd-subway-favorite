package nextstep.auth.intercpetor;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.authentication.UserDetails;
import nextstep.user.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends NonChainFilter {
    private final Logger log = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);

    private final static String USERNAME = "userName";
    private final static String PASSWORD = "password";

    private UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws Exception {
        String userName = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        return new AuthenticationToken(userName, password);
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null) {
            log.info("로그인이 이미 되어있습니다. userName : {}", context.getAuthentication()
                    .getPrincipal());
            throw new AuthenticationException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (userDetails == null) {
            log.info("올바르지 않은 토큰입니다. (존재하지 않는 사용자 - userName : {})", token.getPrincipal());
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            log.info("패스워드가 틀렸습니다. userName : {}", userDetails.getEmail());
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }

    @Override
    public void afterAuthenticated(Authentication authentication, HttpServletResponse response) throws JsonProcessingException, Exception {
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}
