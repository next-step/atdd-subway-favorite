package nextstep.auth.service;

import nextstep.member.domain.LoginMember;

public interface UserDetailsService {

	LoginMember loadUserByUsername(String email);
}
