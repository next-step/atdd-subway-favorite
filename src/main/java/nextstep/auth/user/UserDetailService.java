package nextstep.auth.user;

public interface UserDetailService {
    User loadUserByUsername(String email);
}
