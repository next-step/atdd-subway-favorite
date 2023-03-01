package nextstep.auth.application;

import nextstep.auth.dto.GithubProfileResponse;

public interface GithubClient {
    String getAccessTokenFromGithub(String code);

    GithubProfileResponse getGithubProfileFromGithub(String accessToken);
}
