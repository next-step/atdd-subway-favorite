package nextstep.subway.auth.domain;

public interface User {
    boolean checkPassword(String password);

    Long getId();

    String getUsername();

    String getPassword();
}
