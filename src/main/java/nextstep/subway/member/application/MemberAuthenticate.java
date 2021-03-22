package nextstep.subway.member.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.TokenAuthenticate;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.stereotype.Service;

@Service
public class MemberAuthenticate implements TokenAuthenticate {

    private CustomUserDetailsService customUserDetailsService;

    public MemberAuthenticate(CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember userDetails = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        checkAuthentication(userDetails, authenticationToken);
        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
