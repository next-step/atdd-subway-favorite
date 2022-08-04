package nextstep.auth.authentication;

import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter implements HandlerInterceptor {
    private UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
            String authHeader = new String(Base64.decodeBase64(authCredentials));

            String[] splits = authHeader.split(":");
            String principal = splits[0];
            String credentials = splits[1];

            User user = userDetailsService.loadUserByUsername(principal);
            SaveAuthentication saveAuthentication = new SaveAuthentication(principal, credentials, user);
            saveAuthentication.execute();
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
