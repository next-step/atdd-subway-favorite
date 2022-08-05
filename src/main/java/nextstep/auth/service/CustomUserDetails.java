package nextstep.auth.service;

import nextstep.auth.domain.AuthUser;

public interface CustomUserDetails {
	AuthUser loadUserByUsername(String email);
}
