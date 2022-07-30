package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(BearerTokenAuthenticationFilter.class);
    private JwtTokenProvider jwtTokenProvider;
    private LoginMemberService loginMemberService;
    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider, LoginMemberService loginMemberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null) {
            log.info("로그인이 이미 되어있습니다. userName : {}", context.getAuthentication().getPrincipal());
            return true;
        }

        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if(token.isBlank()){
            return true;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            log.info("올바르지 않은 토큰입니다.");
            throw new AuthenticationException();
        }

        String userName = jwtTokenProvider.getPrincipal(token);
        try{
            loginMemberService.loadUserByUsername(userName);
        } catch (RuntimeException ex){
            log.info("올바르지 않은 토큰입니다. (존재하지 않는 사용자 - userName : {})", userName);
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(userName, jwtTokenProvider.getRoles(token));
        context.setAuthentication(authentication);
        return true;
    }
}
