package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginMemberService loginMemberService;


    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider, LoginMemberService loginMemberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() != null) {
            return true;
        }

        if (token.isBlank() || !jwtTokenProvider.validateToken(token)) {
            return true;
        }

        String principal = jwtTokenProvider.getPrincipal(token);

        try {
            loginMemberService.loadUserByUsername(principal);
        } catch (RuntimeException e) {
            throw new RuntimeException("사용자 정보가 존재하지 않습니다.");
        }

        List<String> roles = jwtTokenProvider.getRoles(token);

        Authentication authentication = new Authentication(principal, roles);
        securityContext.setAuthentication(authentication);
        return true;
    }

}
