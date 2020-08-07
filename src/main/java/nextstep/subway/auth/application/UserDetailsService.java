package nextstep.subway.auth.application;

public interface UserDetailsService {
    public UserDetails loadUserByUsername(String email);
}
