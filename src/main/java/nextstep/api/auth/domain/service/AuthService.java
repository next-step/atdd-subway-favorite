package nextstep.api.auth.domain.service;

import nextstep.api.auth.domain.dto.UserPrincipal;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
public interface AuthService {
	UserPrincipal authenticateWithGithub(String code);
}
