package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final LoginMemberService loginMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String username = request.getParameter(USERNAME);
            String password = request.getParameter(PASSWORD);

            AuthenticationToken token = new AuthenticationToken(username, password);

            LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
