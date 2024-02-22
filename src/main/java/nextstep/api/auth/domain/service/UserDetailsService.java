package nextstep.api.auth.domain.service;

import java.util.Optional;

import nextstep.api.auth.domain.dto.UserPrincipal;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
public interface UserDetailsService {
	Optional<UserPrincipal> loadUserByEmailOptional(String email);
	UserPrincipal loadUserByEmail(String email);
}
