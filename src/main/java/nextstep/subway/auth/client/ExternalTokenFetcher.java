package nextstep.subway.auth.client;

import nextstep.subway.auth.client.dto.ProfileResponse;

public interface ExternalTokenFetcher {

    String requestToken(String code);

    ProfileResponse findUser(String accessToken);
}
