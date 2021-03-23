package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;

public interface TokenAuthenticate {
    public Authentication authenticate(AuthenticationToken authenticationToken);

}
