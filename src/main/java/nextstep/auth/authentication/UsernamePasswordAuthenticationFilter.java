package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            registerAuthentication(request);
            return false;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    private void registerAuthentication(HttpServletRequest request) {
        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);

        LoginMember findMember = loginMemberService.loadUserByUsername(email);
        if (findMember == null) {
            throw new AuthenticationException();
        }

        if (findMember.isInvalidPassword(password)) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(findMember.getEmail(), findMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
