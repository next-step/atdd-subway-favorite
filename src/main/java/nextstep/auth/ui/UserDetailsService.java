package nextstep.auth.ui;

@FunctionalInterface
public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
