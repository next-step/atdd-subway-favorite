package nextstep.auth.user;

public interface UserDetailsService {
    UserDetails loadUserByPrincipal(String principal);
}
