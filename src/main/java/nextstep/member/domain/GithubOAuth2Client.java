package nextstep.member.domain;

import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;

public interface GithubOAuth2Client {

    AccessTokenResponse getAccessToken(String code);

    GithubUserInfoResponse getUserInfo(String accessToken);

}
