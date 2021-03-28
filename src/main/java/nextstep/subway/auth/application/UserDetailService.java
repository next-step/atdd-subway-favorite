package nextstep.subway.auth.application;

public interface UserDetailService {
    UserDetails loadUserByUsername(String email);
}
