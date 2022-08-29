package nextstep.auth.user;

public interface UserDetailService {
    UserDetails loadUserByUsername(String email);
}
