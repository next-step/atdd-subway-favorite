package nextstep.filter;

import nextstep.exception.InvalidAccessTokenException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final String BEARER = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isInvalidBearerToken(bearerToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InvalidAccessTokenException();
        }
        String accessToken = bearerToken.substring(BEARER.length());
        if (!jwtTokenProvider.validateToken(accessToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InvalidAccessTokenException();
        }
        String principal = jwtTokenProvider.getPrincipal(accessToken);
        MemberResponse member = memberService.findMemberByEmail(principal);
        request.setAttribute("member", member);
        return true;
    }

    private static boolean isInvalidBearerToken(String bearerToken) {
        return bearerToken == null || !bearerToken.startsWith(BEARER);
    }
}
