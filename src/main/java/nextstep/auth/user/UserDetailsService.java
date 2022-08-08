package nextstep.auth.user;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
