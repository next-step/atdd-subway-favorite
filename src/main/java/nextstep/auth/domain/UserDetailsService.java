package nextstep.auth.domain;

public interface UserDetailsService {

    UserDetails loadByUserEmail(String email, int age);

    UserDetails loadByUserEmail(String email);
}
