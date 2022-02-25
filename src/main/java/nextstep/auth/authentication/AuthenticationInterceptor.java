package nextstep.auth.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.domain.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailService;
    private final AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(UserDetailService userDetailService,
            AuthenticationConverter authenticationConverter) {
        this.userDetailService = userDetailService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authenticate = authenticate(token);
        afterAuthentication(request, response, authenticate);
        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetails userDetails = userDetailService.loadUserByUsername(principal);
        userDetails.checkPassword(token.getCredentials());

        return new Authentication(userDetails);
    }
    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }
}
