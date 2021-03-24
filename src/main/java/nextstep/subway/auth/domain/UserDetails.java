package nextstep.subway.auth.domain;

public interface UserDetails {

    String getEmail();
    String getPassword();

    boolean validatePassword(String password);

}
