package nextstep.api.auth.application.userdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
