package nextstep.auth.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationNonChainInterceptor implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;

    public AuthenticationNonChainInterceptor(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        UserInformation  userInformation = createPrincipal(request);
        LoginMember loginMember = loginMemberService.loadUserByUsername(userInformation.getEmail());

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(userInformation.getPassword())) {
            throw new AuthenticationException();
        }

        afterAuthentication(response, loginMember);

        return false;
    }

    protected abstract void afterAuthentication(HttpServletResponse response, LoginMember loginMember) throws IOException;

    protected abstract UserInformation createPrincipal(HttpServletRequest request) throws IOException;
}
