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
    private static final Logger log = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() != null) {
            log.info("User information is already saved. User principal is {}", securityContext.getAuthentication().getPrincipal());
            return true;
        }

        LoginMember loginMember = loginMemberService.loadUserByUsername(username);
        if (!loginMember.checkPassword(password)) {
            log.info("The password is not correct. User principal is {}", username);
            return true;
        }

        securityContext.setAuthentication(new Authentication(loginMember.getEmail(), loginMember.getAuthorities()));
        return true;
    }
}
