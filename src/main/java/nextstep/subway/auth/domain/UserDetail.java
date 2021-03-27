package nextstep.subway.auth.domain;

public interface UserDetail {
    Long getId();
    boolean checkPassword(String password);
}
