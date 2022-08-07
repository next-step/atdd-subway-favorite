package nextstep.user;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
