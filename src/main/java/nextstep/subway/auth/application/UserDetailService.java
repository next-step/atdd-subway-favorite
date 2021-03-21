package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.UserDetail;

public interface UserDetailService {
  UserDetail loadUserByUsername(String email);
}
