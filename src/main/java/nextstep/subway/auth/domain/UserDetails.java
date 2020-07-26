package nextstep.subway.auth.domain;

public interface UserDetails {
    boolean checkPassword(String credentials);
}
