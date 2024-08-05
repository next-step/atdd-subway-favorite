package nextstep.member.domain;

import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.application.dto.ApplicationTokenResponse;

public interface OauthClient {

    ApplicationTokenResponse requestToken(String code);

    ResourceResponse requestResource(String accessToken);
}
