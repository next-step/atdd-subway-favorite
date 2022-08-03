package nextstep.auth.authentication;

import nextstep.member.application.LoginMemberService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class LoginFilter implements HandlerInterceptor {

    protected LoginMemberService loginMemberService;

    public LoginFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        login(request, response);
        return false;
    }

    protected abstract void login(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
