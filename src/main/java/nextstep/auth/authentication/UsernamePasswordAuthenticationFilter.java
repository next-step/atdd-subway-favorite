package nextstep.auth.authentication;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private static final String USER_NAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private UserDetailService userDetailService;

    public UsernamePasswordAuthenticationFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String userName = parameterMap.get(USER_NAME_FIELD)[0];
            String password = parameterMap.get(PASSWORD_FIELD)[0];

            AuthenticationToken token = new AuthenticationToken(userName, password);

            LoginMember loginMember = userDetailService.loadUserByUsername(token.getPrincipal());

            if (loginMember == null) {
                throw new AuthenticationException();
            }

            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {

        }

        return false;
    }
}
