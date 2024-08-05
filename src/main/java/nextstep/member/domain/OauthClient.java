package nextstep.member.domain;

import nextstep.member.application.dto.AccessTokenResponse;
import nextstep.member.application.dto.ClientInfo;
import nextstep.member.application.dto.ResourceResponse;

public interface OauthClient {

    AccessTokenResponse requestToken(String code);

    ResourceResponse requestResource(String accessToken);
}
