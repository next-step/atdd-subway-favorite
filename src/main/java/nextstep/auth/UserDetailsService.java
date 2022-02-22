package nextstep.auth;

public interface UserDetailsService {
	User loadUserByUsername(String email);
}
