package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private UserDetailService customUserDetailsService;
    private AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(UserDetailService customUserDetailsService, AuthenticationConverter authenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);

        return false;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember loginMember = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        checkAuthentication(loginMember, authenticationToken);

        return new Authentication(loginMember);
    }

    private void checkAuthentication(LoginMember loginMember, AuthenticationToken authenticationToken) {
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
