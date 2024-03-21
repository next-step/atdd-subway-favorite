package nextstep.auth.application;

import nextstep.auth.application.dto.UserDetailDto;

public interface UserDetailService {
    UserDetailDto findByEmail(String email);

    UserDetailDto findByEmailOrCreate(String email);
}
