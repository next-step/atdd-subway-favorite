package nextstep.auth.authentication;

public interface UserDetails {
    boolean checkPassword(String credentials);
}
