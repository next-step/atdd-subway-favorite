package nextstep.auth.application;

public interface UserDetails {
    String getEmail();
    boolean isEqualPassword(String password);
}
