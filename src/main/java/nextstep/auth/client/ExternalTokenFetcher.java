package nextstep.auth.client;

import nextstep.auth.client.dto.ProfileResponse;

public interface ExternalTokenFetcher {

    String requestToken(String code);

    ProfileResponse findUser(String accessToken);
}
