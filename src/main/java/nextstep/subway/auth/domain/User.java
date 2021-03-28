package nextstep.subway.auth.domain;

public interface User {

    String getEmail();
    String getPassword();

    boolean checkPassword(String password);
}
