package nextstep.member.domain;

import nextstep.member.application.dto.AccessTokenResponse;
import nextstep.member.application.dto.ClientInfo;
import nextstep.member.application.dto.ResourceResponse;

public interface OauthTokenClient {

    AccessTokenResponse requestToken(ClientInfo clientInfo, String code);

    ResourceResponse requestResource(ClientInfo clientInfo, String accessToken);
}
