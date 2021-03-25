package nextstep.subway.auth.domain;

public interface UserDetailsService {

    UserDetails loadUserByUsername(String email);

}
