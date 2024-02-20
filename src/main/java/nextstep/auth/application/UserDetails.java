package nextstep.auth.application;

public interface UserDetails {
    boolean isSamePassword(String password);
    String getEmail();
}
