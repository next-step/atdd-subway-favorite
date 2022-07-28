package nextstep.auth.authentication;


public interface AuthMemberLoader {
    AuthMember loadUserByUsername(String email);
}
