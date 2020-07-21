package nextstep.subway.auth.application;

public interface UserDetailsService {
    UserDetail loadUserByUsername(String email);
}
