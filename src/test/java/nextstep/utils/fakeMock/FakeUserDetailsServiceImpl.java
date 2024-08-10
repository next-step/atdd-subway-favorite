package nextstep.utils.fakeMock;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.application.dto.AuthMember;
import nextstep.auth.application.dto.ProfileResponse;

import static nextstep.utils.dtoMock.GithubResponse.사용자1;

public class FakeUserDetailsServiceImpl implements UserDetailsService {

    private final AuthMember authMember = AuthMember.of(사용자1.getEmail(), 사용자1.getPassword());

    @Override
    public AuthMember findAuthMemberByEmail(String email) {
        return authMember;
    }

    @Override
    public AuthMember findAuthMemberOrOtherJob(ProfileResponse profileResponse) {
        return authMember;
    }
}

