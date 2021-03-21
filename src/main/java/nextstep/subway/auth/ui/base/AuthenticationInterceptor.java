package nextstep.subway.auth.ui.base;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.exception.InvalidCredentialsException;
import nextstep.subway.auth.exception.NotFoundUserException;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor, AuthenticationConverter {

    private final CustomUserDetailsService userDetailsService;

    public AuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);

        afterAuthentication(request, response, authentication);
        return false;
    }

    public abstract void afterAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException;

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new NotFoundUserException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new InvalidCredentialsException();
        }
    }
}
