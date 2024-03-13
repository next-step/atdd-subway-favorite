package nextstep.auth.application;

import nextstep.auth.ui.dto.GithubProfileResponse;

public interface OAuth2Client {

    String requestAccessToken(String code);

    GithubProfileResponse requestUserInfo(String accessToken);
}
