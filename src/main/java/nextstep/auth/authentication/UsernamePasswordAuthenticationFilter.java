package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;
    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            final LoginMember loginMember = findLoginMember(request);
            setAuthentication(loginMember);
        }
        return true;
    }

    private LoginMember findLoginMember(HttpServletRequest request) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final LoginMember loginMember = loginMemberService.loadUserByUsername(username);
        checkPassword(password, loginMember);
        return loginMember;
    }

    private void checkPassword(String password, LoginMember loginMember) {
        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }

    private void setAuthentication(LoginMember loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
