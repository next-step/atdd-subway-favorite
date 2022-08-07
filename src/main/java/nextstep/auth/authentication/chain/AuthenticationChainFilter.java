package nextstep.auth.authentication.chain;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationChainFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AuthenticationToken token = convert(request);

            Authentication authentication = authentication(token);

            SecurityContextMapper.setContext(authentication.getPrincipal().toString(), authentication.getAuthorities());
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public abstract AuthenticationToken convert(HttpServletRequest request);
    public abstract Authentication authentication(AuthenticationToken authenticationToken);
}
