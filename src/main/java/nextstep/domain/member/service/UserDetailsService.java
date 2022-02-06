package nextstep.domain.member.service;

import nextstep.domain.member.domain.LoginMember;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String email);
}
