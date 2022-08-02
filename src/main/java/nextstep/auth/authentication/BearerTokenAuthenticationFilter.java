package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.filter.LoginService;
import nextstep.auth.token.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(BearerTokenAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;


    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider, LoginService loginService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() != null) {
            log.info("User information is already saved. User principal is {}", securityContext.getAuthentication().getPrincipal());
            return true;
        }

        if (token.isBlank()) {
            return true;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            log.info("The Token is not Valid. Token is {}", token);
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);

        if (!loginService.isUserExist(principal)) {
            log.info("The Token is not Valid. Principal is {}", principal);
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(principal, jwtTokenProvider.getRoles(token));
        securityContext.setAuthentication(authentication);
        return true;
    }

}
