package nextstep.auth.authentication;

import nextstep.member.domain.LoginMember;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String email);
}
