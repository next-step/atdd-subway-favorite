package nextstep.auth.service;

public interface UserDetailsService {

    UserDetails loadUserByUsername(String email);
}
