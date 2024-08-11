package nextstep.auth.application;

import nextstep.auth.application.dto.AuthMember;
import nextstep.auth.application.dto.ProfileResponse;

public interface UserDetailsService {
    AuthMember findAuthMemberByEmail(String email);

    AuthMember findAuthMemberOrOtherJob(ProfileResponse profileResponse);
}

