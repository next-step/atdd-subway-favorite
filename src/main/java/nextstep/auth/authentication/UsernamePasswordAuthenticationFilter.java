package nextstep.auth.authentication;

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
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            LoginMember loginMember = loginMemberService.loadUserByUsername(username);
            SaveAuthentication saveAuthentication = new SaveAuthentication(username, password, loginMember);
            saveAuthentication.execute();
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
