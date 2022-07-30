package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null) {
            log.info("로그인이 이미 되어있습니다. userName : {}", context.getAuthentication().getPrincipal());
            return true;
        }

        LoginMember loginMember = loginMemberService.loadUserByUsername(request.getParameter("userName"));
        if (!loginMember.checkPassword(request.getParameter("password"))){
            log.info("패스워드가 틀렸습니다. userName : {}", loginMember.getEmail());
            return false;
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        context.setAuthentication(authentication);

        return true;
    }
}
