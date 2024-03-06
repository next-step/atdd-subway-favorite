package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2ProfileResponse;

public interface UserDetailService {
    UserDetails findMemberByEmail(String email);
    UserDetails findOrCreateMember(OAuth2ProfileResponse res);
}
