package nextstep.auth.application;

import nextstep.auth.domain.UserDetails;

public interface UserDetailService {

    UserDetails loadUserByUsername(String email);
}
