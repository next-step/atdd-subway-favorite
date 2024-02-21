package nextstep.auth.userdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
