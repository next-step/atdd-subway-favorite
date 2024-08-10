package nextstep.member.infrastructure;

import nextstep.member.presentation.TokenResponse;

public interface GithubClient {
    TokenResponse getAccessTokenFromGithub(String code);

    GithubProfileResponse requestGithubProfile(String accessToken);
}
