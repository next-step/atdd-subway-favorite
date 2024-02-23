package nextstep.api.auth.domain.service;

import nextstep.api.auth.application.dto.OAuthUserRegistrationRequest;
import nextstep.api.auth.domain.UserDetails;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
public interface OAuthUserRegistrationService {
	UserDetails registerOAuthUser(OAuthUserRegistrationRequest oAuthUserRegistrationRequest);
}
