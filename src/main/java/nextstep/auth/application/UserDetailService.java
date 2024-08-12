package nextstep.auth.application;

import nextstep.auth.domain.UserDetails;

public interface UserDetailService {
    UserDetails findOrElseGet(String email);

    UserDetails findByEmail(String email);
}
