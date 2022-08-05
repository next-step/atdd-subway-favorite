package nextstep.auth.userdetails;

@FunctionalInterface
public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
