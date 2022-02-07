package nextstep.auth.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.after.AfterAuthentication;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.UserDetails;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter authenticationConverter;
    private final AfterAuthentication afterAuthentication;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);
        afterAuthentication.afterAuthentication(request, response, authentication);
        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
