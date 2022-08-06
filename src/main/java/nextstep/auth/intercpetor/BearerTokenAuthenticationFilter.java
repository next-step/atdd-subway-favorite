package nextstep.auth.intercpetor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends ChainFilter {
    private final Logger log = LoggerFactory.getLogger(BearerTokenAuthenticationFilter.class);
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(token)) {
            log.info("올바르지 않은 토큰입니다.");
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        return new AuthenticationToken(principal, token);
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        String credentials = token.getCredentials();
        List<String> roles = jwtTokenProvider.getRoles(credentials);
        return new Authentication(principal, roles);
    }
}
