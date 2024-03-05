package nextstep.member.application;

import nextstep.member.ui.dto.GithubProfileResponse;

public interface OAuth2Client {

    String requestAccessToken(String code);

    GithubProfileResponse requestUserInfo(String accessToken);
}
