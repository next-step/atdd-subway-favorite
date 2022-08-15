package nextstep.auth.filter;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends AuthenticationFilter {

    private static final String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected Authentication getAuth(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorization) || !authorization.startsWith(BEARER)) {
            throw new AuthenticationException();
        }

        String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthenticationException();
        }

        return new Authentication(
                jwtTokenProvider.getPrincipal(accessToken),
                jwtTokenProvider.getRoles(accessToken)
        );
    }

}
