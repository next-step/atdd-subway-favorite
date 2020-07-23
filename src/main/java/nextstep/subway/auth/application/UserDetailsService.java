package nextstep.subway.auth.application;

public interface UserDetailsService {
    UserDetail convertJsonToUserDetail(String json);

    UserDetail loadUserByUsername(String email);
}
