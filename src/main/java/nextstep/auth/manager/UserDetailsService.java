package nextstep.auth.manager;

public interface UserDetailsService {
    UserMember loadUserByUsername(String principal);
}
