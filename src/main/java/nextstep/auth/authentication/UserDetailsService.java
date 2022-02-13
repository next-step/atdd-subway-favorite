package nextstep.auth.authentication;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
