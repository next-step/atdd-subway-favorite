package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;

public interface GithubClient {

    GithubAccessTokenResponse generateAccessToken(String code);

    GithubProfileResponse getGithubProfile(String githubAccessToken);
}
