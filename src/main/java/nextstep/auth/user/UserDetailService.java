package nextstep.auth.user;

public interface UserDetailService {
    UserDetail loadUserByUsername(String email);
}
