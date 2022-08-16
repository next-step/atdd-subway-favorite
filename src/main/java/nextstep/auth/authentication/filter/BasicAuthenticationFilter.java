package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.token.BasicAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter implements HandlerInterceptor {
    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        Authentication token = new BasicAuthenticationToken(principal, credentials);

        return authenticationManager.authenticate(token);
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = attemptAuthentication(request, response);
        if (authentication == null)
            return false;

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
