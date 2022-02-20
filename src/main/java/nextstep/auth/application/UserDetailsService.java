package nextstep.auth.application;

import nextstep.member.domain.UserDetails;

public interface UserDetailsService {
	UserDetails loadUserByUsername(String email);
}
