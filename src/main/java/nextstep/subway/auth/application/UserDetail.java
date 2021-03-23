package nextstep.subway.auth.application;

public interface UserDetail {

    String getEmail();

    String getPassword();

    boolean checkPassword(String password);
}
