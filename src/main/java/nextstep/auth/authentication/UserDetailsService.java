package nextstep.auth.authentication;

import nextstep.domain.member.domain.LoginMemberImpl;

public interface UserDetailsService {
    LoginMemberImpl loadUserByUsername(String email);
}
