package nextstep.auth.service;

public interface UserDetailsService {

    UserDetail loadUserByUsername(String email);
}
