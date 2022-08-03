package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        setAuthentication(request);
        return true;
    }

    public void setAuthentication(HttpServletRequest request) {
        String bearerToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (bearerToken != Strings.EMPTY) {
            validatation(bearerToken);

            Authentication authentication = new Authentication(
                jwtTokenProvider.getPrincipal(bearerToken), jwtTokenProvider.getRoles(bearerToken));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void validatation(String bearerToek) {
        if (!jwtTokenProvider.validateToken(bearerToek)) {
            throw new AuthenticationException();
        }
    }

}
