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
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() != null) {
            log.info("User information is already saved. User principal is {}", securityContext.getAuthentication().getPrincipal());
            throw new AuthenticationException();
        }

        LoginMember loginMember = loginMemberService.loadUserByUsername(request.getParameter(USERNAME));

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(request.getParameter(PASSWORD))) {
            log.info("The password is not correct. User principal is {}", request.getParameter(USERNAME));
            throw new AuthenticationException();
        }

        securityContext.setAuthentication(new Authentication(loginMember.getEmail(), loginMember.getAuthorities()));
        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }
}
