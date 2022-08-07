package nextstep.auth.service;

import org.springframework.stereotype.Service;

import nextstep.auth.domain.CustomUser;

@Service
public interface CustomUserDetails {
	CustomUser loadUserByEmail(String email);
}
