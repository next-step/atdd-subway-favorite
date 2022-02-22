package nextstep.auth.user;

@FunctionalInterface
public interface UserDetailsService {
  UserDetail loadUserByUsername(String email);
}
