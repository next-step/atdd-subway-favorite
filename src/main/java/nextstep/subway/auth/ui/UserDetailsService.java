package nextstep.subway.auth.ui;

public interface UserDetailsService {

    UserDetails loadUserByUsername(String email);
}
