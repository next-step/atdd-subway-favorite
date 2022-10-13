package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.interceptor.AuthNotChainInterceptor;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UsernamePasswordAuthFilter extends AuthNotChainInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthFilter(final UserDetailService userDetailService) {
        super(userDetailService);
    }

    @Override
    protected AuthenticationToken createAuthToken(final HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String username = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];
        return new AuthenticationToken(username, password);
    }

    @Override
    protected void afterSuccessUserCheck(final HttpServletRequest request, final HttpServletResponse response, UserDetail user) {
        Authentication authentication = new Authentication(user.getEmail(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
