package nextstep.login.infra;

import nextstep.login.application.dto.response.GithubProfileResponse;

public interface SocialClient {

    String getAccessTokenFromGithub(String code);

    GithubProfileResponse getGithubProfileFromGithub(String accessToken);
}
