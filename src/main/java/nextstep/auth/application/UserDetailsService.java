package nextstep.auth.application;

public interface UserDetailsService {

    UserDetails findMemberByEmail(String email);

    UserDetails findOrCreateMember(String email, int age);
}
