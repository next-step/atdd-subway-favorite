package nextstep.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class BearerAuthInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthorizationExtractor authorizationExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        String bearerToken = authorizationExtractor.extract(request, AUTHORIZATION_TYPE);

        return jwtTokenProvider.validateToken(bearerToken);
    }
}
