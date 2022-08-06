package nextstep.auth.service;

import org.springframework.stereotype.Service;

import nextstep.auth.domain.AuthUser;

@Service
public interface CustomUserDetails {
	AuthUser loadUserByUsername(String email);
}
