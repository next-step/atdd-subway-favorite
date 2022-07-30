package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (accessToken.isBlank()) {
            return true;
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(jwtTokenProvider.getPrincipal(accessToken), jwtTokenProvider.getRoles(accessToken));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
