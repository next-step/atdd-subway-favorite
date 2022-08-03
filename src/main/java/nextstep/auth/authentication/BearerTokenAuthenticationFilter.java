package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenAuthenticationFilter extends AuthenticationFilter {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected boolean authenticate(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (authentication != null && Strings.isBlank(accessToken)) {
            return true;
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthenticationException();
        }
        authentication = new Authentication(jwtTokenProvider.getPrincipal(accessToken), jwtTokenProvider.getRoles(accessToken));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }
}
