package nextstep.auth.authentication.user;

public interface UserDetailsService {

  UserDetails loadUserByUsername(String email);
}
