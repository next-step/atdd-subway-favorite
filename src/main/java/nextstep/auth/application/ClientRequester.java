package nextstep.auth.application;

import nextstep.auth.application.dto.ProfileResponse;

public interface ClientRequester {
    String requestAccessToken(String code);

    ProfileResponse requestProfile(String accessToken);
}

