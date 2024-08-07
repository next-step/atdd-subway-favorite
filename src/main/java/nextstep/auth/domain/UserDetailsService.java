package nextstep.auth.domain;

public interface UserDetailsService {

    UserDetails loadByUserEmail(String email);
}
