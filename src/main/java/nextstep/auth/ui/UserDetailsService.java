package nextstep.auth.ui;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
