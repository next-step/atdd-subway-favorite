package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetails;
import org.springframework.stereotype.Service;

@Service
public
class MemberTokenAuthenticate implements TokenAuthenticate {

    private LoginMemberPort memberLoader;

    public MemberTokenAuthenticate(LoginMemberPort memberLoader){
        this.memberLoader = memberLoader;
    }

    @Override
    public Authentication authenticate(AuthenticationToken authenticationToken) {
        UserDetails userDetails = memberLoader.getLoginMember(authenticationToken.getPrincipal());
        checkAuthentication(userDetails, authenticationToken);
        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
