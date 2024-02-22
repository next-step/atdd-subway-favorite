package nextstep.api.auth.domain.service;

import nextstep.api.auth.domain.UserDetails;
import nextstep.api.auth.application.dto.OAuthUserRegistrationRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
public interface OAuthUserRegistrationService {
	UserDetails registerOAuthUser(OAuthUserRegistrationRequest oAuthUserRegistrationRequest);
}
