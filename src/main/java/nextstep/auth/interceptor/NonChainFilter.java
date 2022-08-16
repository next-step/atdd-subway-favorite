package nextstep.auth.interceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.user.User;
import nextstep.member.user.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class NonChainFilter implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;

    protected abstract User preAuthentication(HttpServletRequest request) throws IOException;

    protected abstract void afterAuthentication(User user, HttpServletResponse response) throws IOException;

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            User user = preAuthentication(request);
            afterAuthentication(user, response);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    protected User findAuthMember(String email, String password) {
        User user = userDetailsService.loadUserByUsername(email);

        if (user == null) {
            throw new AuthenticationException();
        }

        if (isNotMatchPassword(password, user)) {
            throw new AuthenticationException();
        }
        return user;
    }


    private boolean isNotMatchPassword(String password, User user) {
        return !user.checkPassword(password);
    }


}
