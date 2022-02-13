package nextstep.auth.authentication;

import nextstep.auth.context.DetailMember;

public interface UserDetailService {
    DetailMember loadUserByUsername(String email);
}
