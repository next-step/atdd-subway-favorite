package nextstep.auth.service;

import nextstep.auth.domain.AuthenticatedMember;

public interface UserDetailsService {

	AuthenticatedMember loadUserByUsername(String email);
}
