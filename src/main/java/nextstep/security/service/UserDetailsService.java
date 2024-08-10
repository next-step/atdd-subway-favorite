package nextstep.security.service;

import nextstep.security.domain.Authentication;
import nextstep.security.domain.UserInfo;

public interface UserDetailsService<CREDENTIALS, PRINCIPAL> {

  Authentication<CREDENTIALS, PRINCIPAL> loadUserByUsername(
      UserInfo<CREDENTIALS, PRINCIPAL> userInfo);

}
