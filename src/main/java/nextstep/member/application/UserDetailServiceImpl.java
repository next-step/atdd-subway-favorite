package nextstep.member.application;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.UserDetail;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

  @Override
  public UserDetail findByEmail(String email) {
    return null;
  }

  @Override
  public UserDetail createUser(String email, String password, int age) {
    return null;
  }
}
