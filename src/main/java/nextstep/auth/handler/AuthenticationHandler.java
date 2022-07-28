package nextstep.auth.handler;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class AuthenticationHandler implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;

    protected abstract LoginMember preAuthentication(HttpServletRequest request) throws IOException;

    protected abstract void afterAuthentication(LoginMember loginMember, HttpServletResponse response) throws IOException;

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginMember loginMember = preAuthentication(request);
        afterAuthentication(loginMember, response);

        return false;
    }

    protected LoginMember findLoginMember(String email, String password) {
        LoginMember loginMember = loginMemberService.loadUserByUsername(email);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (isNotMatchPassword(password, loginMember)) {
            throw new AuthenticationException();
        }
        return loginMember;
    }


    private boolean isNotMatchPassword(String password, LoginMember loginMember) {
        return !loginMember.checkPassword(password);
    }


}
