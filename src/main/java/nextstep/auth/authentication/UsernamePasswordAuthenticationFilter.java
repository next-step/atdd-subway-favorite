package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private final LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var username = request.getParameter(USERNAME_FIELD);
        var password = request.getParameter(PASSWORD_FIELD);

        var loginMember = loginMemberService.loadUserByUsername(username);

        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }

        var authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
