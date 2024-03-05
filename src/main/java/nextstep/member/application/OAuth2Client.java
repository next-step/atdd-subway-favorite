package nextstep.member.application;

import nextstep.member.application.dto.OAuth2ProfileResponse;

public interface OAuth2Client {
    String requestGithubToken(String code);
    OAuth2ProfileResponse requestGithubProfile(String accessToken);
}
