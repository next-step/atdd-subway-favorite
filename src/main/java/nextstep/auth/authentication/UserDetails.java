package nextstep.auth.authentication;

public interface UserDetails {
    String getUsername();

    String getPassword();

    boolean checkPassword(String credentials);
}
