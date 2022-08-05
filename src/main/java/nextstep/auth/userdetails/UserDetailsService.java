package nextstep.auth.userdetails;

public interface UserDetailsService {

    public UserDetails loadUserByUsername(String email);
}
