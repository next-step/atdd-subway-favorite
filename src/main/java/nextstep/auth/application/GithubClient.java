package nextstep.auth.application;

import nextstep.auth.infrastructure.GithubAccessTokenResponse;
import nextstep.auth.infrastructure.GithubProfileResponse;

public interface GithubClient {
    GithubAccessTokenResponse getAccessTokenFromGithub(String code);

    GithubProfileResponse requestGithubProfile(String accessToken);
}
