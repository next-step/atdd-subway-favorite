package nextstep.auth.authentication;

public interface UserDetailService {
    UserDetails loadUserByUsername(String email);
}
