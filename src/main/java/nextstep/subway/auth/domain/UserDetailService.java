package nextstep.subway.auth.domain;

public interface UserDetailService {
    UserDetail loadUserByUsername(String email);
}
