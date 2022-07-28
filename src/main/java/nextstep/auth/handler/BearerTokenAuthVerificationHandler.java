package nextstep.auth.handler;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@RequiredArgsConstructor
public class BearerTokenAuthVerificationHandler extends AuthVerificationHandler {

    private static final String TOKEN_TYPE = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestTokenHeader == null || !requestTokenHeader.startsWith(TOKEN_TYPE)) {
            throw new AuthenticationException();
        }
        String token = requestTokenHeader.replaceFirst(TOKEN_TYPE, EMPTY);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        return new Authentication(jwtTokenProvider.getPrincipal(token), jwtTokenProvider.getRoles(token));
    }
}
