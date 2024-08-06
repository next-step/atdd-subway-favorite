package nextstep.auth;

public interface TokenAuthorizer {
    LoginMember authorize(String token);
}
