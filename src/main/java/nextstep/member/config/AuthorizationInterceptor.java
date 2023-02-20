package nextstep.member.config;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.exception.InvalidTokenException;
import nextstep.member.application.exception.MemberErrorCode;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String bearerAccessToken = request.getHeader(AUTHORIZATION_HEADER);

        String accessToken = BearerTokenExtractor.extract(bearerAccessToken);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidTokenException(MemberErrorCode.INVALID_TOKEN);
        }

        return true;
    }
}
