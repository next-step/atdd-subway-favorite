package nextstep.auth.domain;

public interface UserDetails {
    void checkPassword(String password);
}
