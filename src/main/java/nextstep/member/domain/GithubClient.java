package nextstep.member.domain;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;

public interface GithubClient {
    GithubAccessTokenResponse getAccessTokenFromGithub(GithubAccessTokenRequest request);

    GithubProfileResponse getUserProfileFromGithub(String accessToken);
}
