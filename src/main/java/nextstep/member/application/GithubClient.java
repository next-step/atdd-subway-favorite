package nextstep.member.application;

import nextstep.member.infrastructure.GithubProfileResponse;

public interface GithubClient {
    TokenResponse getAccessTokenFromGithub(String code);

    GithubProfileResponse requestGithubProfile(String accessToken);
}
