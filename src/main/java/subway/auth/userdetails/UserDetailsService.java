package subway.auth.userdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
