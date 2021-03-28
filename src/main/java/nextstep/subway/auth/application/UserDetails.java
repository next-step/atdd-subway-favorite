package nextstep.subway.auth.application;

public interface UserDetails {
    boolean checkPassword(String password);
}
