package nextstep.auth.interceptor;

import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationNonChainHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        LoginMember loginMember = createAuthentication(request);
        afterHandle(loginMember, response);
        return false;

    }

    protected abstract LoginMember createAuthentication(HttpServletRequest request);

    protected abstract void afterHandle(LoginMember loginMember, HttpServletResponse response);
}
