package nextstep.auth.authentication;

@FunctionalInterface
public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
