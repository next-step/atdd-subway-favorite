package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

// 템플릿 메서드 패턴
public abstract class AuthenticationInterceptor implements HandlerInterceptor, AuthenticationConverter {

    private final UserDetailsService userDetailsService;

    public AuthenticationInterceptor(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws IOException {
        final AuthenticationToken token = convert(request);
        final Authentication authentication = authenticate(token);
        afterAuthentication(request, response, authentication);
        return false;
    }

    protected abstract void afterAuthentication(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final Authentication authentication) throws IOException;

    protected Authentication authenticate(final AuthenticationToken token) {
        final String principal = token.getPrincipal();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);
        return new Authentication(userDetails);
    }

    private void checkAuthentication(final UserDetails userDetails, final AuthenticationToken token) {
        if (Objects.isNull(userDetails)) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
