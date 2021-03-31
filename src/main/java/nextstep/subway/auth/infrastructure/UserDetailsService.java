package nextstep.subway.auth.infrastructure;

public interface UserDetailsService {

    UserDetails loadUserByUsername(String email);
}
