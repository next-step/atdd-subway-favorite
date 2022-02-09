package nextstep.auth;

@FunctionalInterface
public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
