package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2ProfileResponse;

public interface OAuth2Client {
    String requestToken(String code);
    OAuth2ProfileResponse requestProfile(String accessToken);
}
