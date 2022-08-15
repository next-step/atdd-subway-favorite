package nextstep.member.user;

public interface UserDetailsService {

    User loadUserByUsername(String email);

}
