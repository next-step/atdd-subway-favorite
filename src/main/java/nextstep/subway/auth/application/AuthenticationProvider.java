package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.exception.InvalidAuthenticationTokenException;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public AuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    public void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new InvalidAuthenticationTokenException("unknown user");
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new InvalidAuthenticationTokenException("password does not match");
        }
    }
}
