package nextstep.auth.authentication;

@FunctionalInterface
public interface UserDetailService {
    User loadUserByUsername(String username);
}