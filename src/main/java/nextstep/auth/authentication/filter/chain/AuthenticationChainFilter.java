package nextstep.auth.authentication.filter.chain;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public abstract class AuthenticationChainFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken token = createToken(request);
        if (Objects.isNull(token)) {
            return true;
        }

        Authentication authentication = authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    protected abstract AuthenticationToken createToken(HttpServletRequest request) throws IOException;

    protected abstract Authentication authenticate(AuthenticationToken token);

}
