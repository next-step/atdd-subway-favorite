package nextstep.auth.application;

import nextstep.auth.infrastructure.GithubProfileResponse;

public interface GithubClient {
    TokenResponse getAccessTokenFromGithub(String code);

    GithubProfileResponse requestGithubProfile(String accessToken);
}
