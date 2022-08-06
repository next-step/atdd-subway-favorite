package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final LoginMemberService loginMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            var userName = request.getParameter(USERNAME);
            var password = request.getParameter(PASSWORD);

            LoginMember loginMember = loginMemberService.loadUserByUsername(userName);

            if (loginMember == null) {
                throw new AuthenticationException();
            }

            checkPassword(loginMember, password);

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void checkPassword(LoginMember loginMember, String password) {
        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
