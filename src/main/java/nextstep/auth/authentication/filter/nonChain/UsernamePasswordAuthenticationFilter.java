package nextstep.auth.authentication.filter.nonChain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends NonChainInterceptor {
    private static final String USER_NAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final int FIRST_INDEX = 0;
    private UserDetailService userDetailService;

    public UsernamePasswordAuthenticationFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String userName = parameterMap.get(USER_NAME_FIELD)[FIRST_INDEX];
        String password = parameterMap.get(PASSWORD_FIELD)[FIRST_INDEX];

        return new AuthenticationToken(userName, password);
    }

    @Override
    protected Authentication getAuthentication(AuthenticationToken authenticationToken) {
        UserDetails userDetails = userDetailService.loadUserByUsername(authenticationToken.getPrincipal());

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }

    @Override
    protected void doAuthentication(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
