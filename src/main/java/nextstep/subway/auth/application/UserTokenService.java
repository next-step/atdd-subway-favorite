package nextstep.subway.auth.application;

import nextstep.subway.auth.application.dto.UserTokenRequest;

public interface UserTokenService {
    public String createToken(UserTokenRequest userTokenRequest);
}
