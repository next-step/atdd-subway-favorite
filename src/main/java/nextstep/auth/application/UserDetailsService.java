package nextstep.auth.application;

public interface UserDetailsService {
    UserDetails findMemberByEmail(String email);
    UserDetails findOrCreate(String email);
}
