package nextstep.subway.auth.application;

public interface UserDetails {
    String getEmail();

    String getPassword();

    boolean checkPassword(String password);
}
