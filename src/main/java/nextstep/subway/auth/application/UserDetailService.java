package nextstep.subway.auth.application;

public interface UserDetailService {
    UserDetail loadUserByUserName(String userName);
}
