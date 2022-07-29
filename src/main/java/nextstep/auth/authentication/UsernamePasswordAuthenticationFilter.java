package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");

        final LoginMember loginMember = loginMemberService.loadUserByUsername(email);

        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }

        final Authentication authentication = new Authentication(email, loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }
}
