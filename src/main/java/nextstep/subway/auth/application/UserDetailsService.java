package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.UserDetail;

public interface UserDetailsService {
    UserDetail findMemberByEmail(String email);
    void findMemberByEmailNotExistSave(String email);
}
