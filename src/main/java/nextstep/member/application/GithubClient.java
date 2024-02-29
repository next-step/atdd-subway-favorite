package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;

public interface GithubClient {

    GithubAccessTokenResponse generateAccessToken(String code);

    GithubProfileResponse getGithubProfile(String githubAccessToken);
}
