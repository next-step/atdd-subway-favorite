package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.User;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends Authorizator {

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        AuthenticationToken token = getToken(request);
        User loginMember = userDetailsService.loadUserByUsername(token.getPrincipal());

        checkAuthentication(token, loginMember);

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }


    private AuthenticationToken getToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    private void checkAuthentication(AuthenticationToken token, User user) {
        if (!user.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
