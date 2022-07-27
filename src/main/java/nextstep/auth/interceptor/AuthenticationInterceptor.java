package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthMember;
import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.converter.AuthenticationConverter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationConverter authenticationConverter;
    private final AuthMemberLoader authMemberLoader;

    public AuthenticationInterceptor(AuthenticationConverter authenticationConverter, AuthMemberLoader authMemberLoader) {
        this.authenticationConverter = authenticationConverter;
        this.authMemberLoader = authMemberLoader;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        afterAuthentication(request, response, authentication);
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return authenticationConverter.convert(request);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        AuthMember authMember = authMemberLoader.loadUserByUsername(principal);
        checkAuthentication(authMember, authenticationToken);

        return new Authentication(authMember.getEmail(), authMember.getAuthorities());
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    private void checkAuthentication(AuthMember authMember, AuthenticationToken authenticationToken) {
        if (authMember == null) {
            throw new AuthenticationException();
        }

        if (!authMember.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
