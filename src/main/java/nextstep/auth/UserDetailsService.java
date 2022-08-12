package nextstep.auth;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
