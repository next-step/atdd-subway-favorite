package nextstep.subway.auth.ui;

import nextstep.subway.auth.dto.UserPrincipal;

public interface UserLoader {
    public UserPrincipal loadUserPrincipal(String principal);
}
