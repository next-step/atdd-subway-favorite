package nextstep.auth.authentication;

@FunctionalInterface
public interface UserDetailService {
    UserDetails loadUserByUsername(String username);
}