package nextstep.member.infra;

import nextstep.member.infra.dto.GithubProfileResponse;

public interface GithubClient {

    String getAccessTokenFromGithub(String code);

    GithubProfileResponse getGithubProfileFromGithub(String accessToken);
}
