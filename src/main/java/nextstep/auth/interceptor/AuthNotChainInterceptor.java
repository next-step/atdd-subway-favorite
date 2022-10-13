package nextstep.auth.interceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class AuthNotChainInterceptor implements HandlerInterceptor {
    protected final UserDetailService userDetailService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler){
        try {
            AuthenticationToken authToken = createAuthToken(request);
            checkValidAuth(authToken);
            UserDetail user = getUserDetail(authToken);
            afterSuccessUserCheck(request, response, user);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract AuthenticationToken createAuthToken(final HttpServletRequest request) throws IOException;

    protected abstract void afterSuccessUserCheck(final HttpServletRequest request, final HttpServletResponse response, UserDetail user)
        throws IOException;

    protected void checkValidAuth(AuthenticationToken authToken){
        UserDetail loginMember = getUserDetail(authToken);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(authToken.getCredentials())) {
            throw new AuthenticationException();
        }
    }

    private UserDetail getUserDetail(final AuthenticationToken authToken) {
        return userDetailService.loadUserByUsername(authToken.getPrincipal());
    }
}
