package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private final LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // TODO: 구현하세요.
        try {
            String username = request.getParameter(USERNAME_FIELD);
            String password = request.getParameter(PASSWORD_FIELD);

            AuthenticationToken token = new AuthenticationToken(username, password);

            LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

            if (loginMember == null) {
                throw new AuthenticationException();
            }

            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
