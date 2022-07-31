package nextstep.auth.user;


public interface UserDetailsService {
    User loadUserByUsername(String email);
}
