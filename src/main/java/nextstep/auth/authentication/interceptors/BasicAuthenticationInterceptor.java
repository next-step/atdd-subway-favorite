package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationInterceptor extends ChainingAuthenticationInterceptor {
    private final UserDetailsService UserDetailsService;

    public BasicAuthenticationInterceptor(UserDetailsService UserDetailsService) {
        this.UserDetailsService = UserDetailsService;
    }

    @Override
    AuthenticateRequest createLoginRequest(final HttpServletRequest request) {
        final String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        final String authHeader = new String(Base64.decodeBase64(authCredentials));

        final String[] splits = authHeader.split(":");
        final String principal = splits[0];
        final String credentials = splits[1];

        return new AuthenticateRequest(principal, credentials);
    }

    @Override
    UserDetails findUserDetails(final AuthenticateRequest request) {
        final UserDetails userDetails = UserDetailsService.loadUserByUsername(request.getEmail());
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        return userDetails;
    }

    @Override
    boolean isAuthenticated(final AuthenticateRequest authenticateRequest, final UserDetails userDetails) {
        return userDetails.checkPassword(authenticateRequest.getPassword());
    }

}
