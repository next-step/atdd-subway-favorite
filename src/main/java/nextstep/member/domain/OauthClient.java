package nextstep.member.domain;

import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.application.dto.TokenResponse;

public interface OauthClient {

    TokenResponse requestToken(String code);

    ResourceResponse requestResource(String accessToken);
}
