package nextstep.subway.auth.domain;

public interface UserDetails {

    Long getId();

    boolean checkPassword(String password);
}
