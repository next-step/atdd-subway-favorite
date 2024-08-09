package nextstep.member.application;

import nextstep.member.application.dto.ProfileResponse;

public interface ClientRequester {
    String requestGithubAccessToken(String code);
    ProfileResponse requestGithubProfile(String accessToken);
}
