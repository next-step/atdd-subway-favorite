package nextstep.auth.domain;

import nextstep.auth.application.dto.ResourceResponse;
import nextstep.member.application.dto.ApplicationTokenResponse;

public interface OauthClient {

    ApplicationTokenResponse requestToken(String code);

    ResourceResponse requestResource(String accessToken);
}
