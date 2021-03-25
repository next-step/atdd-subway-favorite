package nextstep.subway.auth.dto;

public interface UserDetails {
    boolean checkPassword(String password);
}
