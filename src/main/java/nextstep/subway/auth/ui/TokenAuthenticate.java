package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;

public interface TokenAuthenticate {
    Authentication authenticate(AuthenticationToken authenticationToken);
}
