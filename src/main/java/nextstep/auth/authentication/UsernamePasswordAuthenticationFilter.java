package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private static final String NOT_MATCH_EMAIL_PASSWORD = "이메일과 비밀번호가 일치하지 않습니다.";

    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LoginMember member = loginMemberService.loadUserByUsername(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException(NOT_MATCH_EMAIL_PASSWORD);
        }

        Authentication authentication = new Authentication(email, member.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
