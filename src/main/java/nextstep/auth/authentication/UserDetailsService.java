package nextstep.auth.authentication;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String email);
}
