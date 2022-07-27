package nextstep.auth.authentication;

import static org.apache.logging.log4j.util.Strings.EMPTY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter implements HandlerInterceptor {

    private static final String TOKEN_TYPE = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = getValidToken(request);
        setAuthentication(token);

        return true;
    }

    private String getValidToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!requestTokenHeader.startsWith(TOKEN_TYPE)) {
            throw new AuthenticationException();
        }
        String token = requestTokenHeader.replaceFirst(TOKEN_TYPE, EMPTY);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        return token;
    }

    private void setAuthentication(String token) {
        Authentication authentication = new Authentication(jwtTokenProvider.getPrincipal(token), jwtTokenProvider.getRoles(token));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
