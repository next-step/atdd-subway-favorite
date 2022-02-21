package nextstep.auth.adapter.in;

@FunctionalInterface
public interface UserDetailsService {
  UserDetail loadUserByUsername(String email);
}
