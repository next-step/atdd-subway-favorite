package nextstep.auth.application;

import java.util.Optional;
import nextstep.auth.domain.UserDetail;

public interface UserDetailService {

  Optional<UserDetail> findByEmail(String email);

  UserDetail createUser(String email, String password, int age);
}
