package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;

public interface GithubClient {
    String getAccessTokenFromGithub(String code);

    GithubProfileResponse getGithubProfileFromGithub(String accessToken);
}
