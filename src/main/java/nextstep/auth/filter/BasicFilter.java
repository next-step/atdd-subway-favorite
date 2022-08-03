package nextstep.auth.filter;

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
    public boolean validToken(String token) {
        return token.contains(COLON);
    }

    @Override
    public Authentication getAuthentication(String token) {
        String[] splits = token.split(COLON);
        String principal = splits[0];
        String credentials = splits[1];
        return new Authentication(principal, credentials);
    }

    @Override
    public Authentication getAuthentication(Authentication authentication) {
        String principal = (String) authentication.getPrincipal();
        LoginMember member = loginService.loadUserByUsername(principal);
        return new Authentication(member.getEmail(), member.getAuthorities());
    }

    @Override
    public boolean validUser(Authentication authentication) {
        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        LoginMember member = loginService.loadUserByUsername(principal);

        return member != null && member.checkPassword(credentials);
    }

}
