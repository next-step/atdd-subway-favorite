package nextstep.member.domain;

import nextstep.member.payload.AccessTokenRequest;
import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;

public interface GithubOAuth2Client {

    AccessTokenResponse getAccessToken(AccessTokenRequest request);

    GithubUserInfoResponse getUserInfo(String accessToken);

}
