package nextstep.auth.user;

import nextstep.member.domain.LoginMember;

public interface UserDetailsService {

	LoginMember loadUserByUsername(String email);
}
