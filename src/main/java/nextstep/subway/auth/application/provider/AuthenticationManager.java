package nextstep.subway.auth.application.provider;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;

public interface AuthenticationManager {
    Authentication authenticate(AuthenticationToken authenticationToken);
}
