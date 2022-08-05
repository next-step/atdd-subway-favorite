package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicFilter implements AuthorizationStrategy {
    private static final String COLON = ":";
    private final LoginService loginService;

    public BasicFilter(LoginService loginService) {
        this.loginService = loginService;
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
        LoginMember member = loginService.loadUserByUsername((String) user.getPrincipal());

        if (!validUser(user, member)) {
            throw new AuthenticationException();
        }

        return new Authentication(member.getEmail(), member.getAuthorities());
    }

    public boolean validToken(String token) {
        return token.contains(COLON);
    }

    public boolean validUser(Authentication user, LoginMember member) {
        return user != null && member.checkPassword((String) user.getCredentials());
    }
}
