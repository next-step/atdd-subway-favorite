package nextstep.subway.member.client;

import nextstep.subway.member.client.dto.ProfileResponse;

public interface ExternalTokenFetcher {

    String requestToken(String code);

    ProfileResponse findUser(String accessToken);
}
