package nextstep.member.application;

import nextstep.member.infrastructure.GithubProfileResponse;
import nextstep.member.presentation.TokenResponse;

public interface GithubClient {
    TokenResponse getAccessTokenFromGithub(String code);

    GithubProfileResponse requestGithubProfile(String accessToken);
}
