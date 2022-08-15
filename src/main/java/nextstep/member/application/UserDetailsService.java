package nextstep.member.application;

import nextstep.member.domain.LoginMember;

public interface UserDetailsService {

    LoginMember loadUserByUsername(String email);
}
