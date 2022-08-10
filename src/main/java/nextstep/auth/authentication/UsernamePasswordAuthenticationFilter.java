package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final LoginMemberService loginMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        LoginMember loginMember = getLoginMember(new AuthenticationToken(username, password));
        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }
        setAuthentication(loginMember);
        return true;
    }

    private void setAuthentication(LoginMember loginMember) {
        Authentication auth = new Authentication(
                loginMember.getEmail(),
                loginMember.getAuthorities()
        );
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    private LoginMember getLoginMember(AuthenticationToken token) {
        try {
            return loginMemberService.loadUserByUsername(token.getPrincipal());
        } catch (NoSuchElementException e) {
            throw new AuthenticationException();
        }
    }

}
