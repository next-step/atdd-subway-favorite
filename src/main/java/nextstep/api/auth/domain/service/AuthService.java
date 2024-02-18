package nextstep.api.auth.domain.service;

import nextstep.api.auth.domain.dto.outport.OAuthUserInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
public interface AuthService {
	OAuthUserInfo  authenticateWithGithub(String code);
}
