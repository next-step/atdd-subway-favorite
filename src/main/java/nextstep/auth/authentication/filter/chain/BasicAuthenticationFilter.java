package nextstep.auth.authentication.filter.chain;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter extends ChainInterceptor {
    private UserDetailService userDetailService;

    public BasicAuthenticationFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected Authentication getAuthentication(AuthenticationToken authenticationToken) {
        LoginMember loginMember = userDetailService.loadUserByUsername(authenticationToken.getPrincipal());
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }

    @Override
    protected void doAuthentication(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
