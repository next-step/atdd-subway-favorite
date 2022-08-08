package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String username = paramMap.get(USERNAME_FIELD)[0];
            String password = paramMap.get(PASSWORD_FIELD)[0];

            AuthenticationToken token = new AuthenticationToken(username, password);

            String principal = token.getPrincipal();
            LoginMember loginMember = loginMemberService.loadUserByUsername(principal);

            if (loginMember == null) {
                throw new AuthenticationException();
            }

            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
