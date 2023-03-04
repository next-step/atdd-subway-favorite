package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;

public interface GithubClient {
    String getAccessTokenFromGithub(String code);

    GithubProfileResponse getGithubProfileFromGithub(String accessToken);
}
