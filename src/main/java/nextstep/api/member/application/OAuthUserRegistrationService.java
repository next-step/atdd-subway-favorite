package nextstep.api.member.application;

import nextstep.api.member.domain.Member;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
public interface OAuthUserRegistrationService {
	Member registerOAuthUser(OAuthUserRegistrationRequest oAuthUserRegistrationRequest);
}
