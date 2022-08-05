package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.member.UserDetailService;
import nextstep.auth.member.UserDetails;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicFilter implements AuthorizationStrategy {
    private static final String COLON = ":";
    private final UserDetailService userDetailService;

    public BasicFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        return new String(Base64.decodeBase64(authCredentials));
    }

    @Override
    public Authentication getAuthentication(String token) {
        String[] splits = token.split(COLON);
        String principal = splits[0];
        String credentials = splits[1];
        return new Authentication(principal, credentials);
    }

    @Override
    public Authentication extractAuthentication(String token) {
        if (!validToken(token)) {
            throw new AuthenticationException();
        }

        Authentication user = getAuthentication(token);
        UserDetails userDetails = userDetailService.loadUserByUsername((String) user.getPrincipal());

        if (!validUser(user, userDetails)) {
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }

    public boolean validToken(String token) {
        return token.contains(COLON);
    }

    public boolean validUser(Authentication user, UserDetails member) {
        return user != null && member.checkPassword((String) user.getCredentials());
    }
}
