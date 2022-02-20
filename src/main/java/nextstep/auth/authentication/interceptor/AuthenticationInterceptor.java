package nextstep.auth.authentication.interceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.convertor.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    protected final AuthenticationConverter converter;
    private final CustomUserDetailsService userDetailsService;

    public AuthenticationInterceptor(CustomUserDetailsService userDetailsService, AuthenticationConverter converter) {
        this.userDetailsService = userDetailsService;
        this.converter = converter;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = converter.convert(request);
        Authentication authentication = authenticate(token);

        afterAuthentication(request,response,authentication);
        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
