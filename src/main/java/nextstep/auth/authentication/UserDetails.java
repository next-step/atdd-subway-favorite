package nextstep.auth.authentication;

public interface UserDetails {
    boolean checkPassword(String password);
    Long getId();
    String getEmail();
}
