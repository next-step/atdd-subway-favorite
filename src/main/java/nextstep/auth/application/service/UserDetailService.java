package nextstep.auth.application.service;

import java.util.Optional;
import nextstep.auth.application.domain.CustomUserDetail;

public interface UserDetailService {

    Optional<CustomUserDetail> findById(String id);

    CustomUserDetail loadUserDetail(String id);

}
