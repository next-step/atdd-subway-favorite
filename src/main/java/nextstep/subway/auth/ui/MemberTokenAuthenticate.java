package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.LoginMemberPrincipal;
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
        LoginMember loginMember = memberLoader.getLoginMember(authenticationToken.getPrincipal());
        checkAuthentication(loginMember, authenticationToken);
        return new Authentication(LoginMemberPrincipal.of(loginMember));
    }

    private void checkAuthentication(LoginMember loginMember, AuthenticationToken token) {
        if (loginMember == null) {
            throw new RuntimeException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
