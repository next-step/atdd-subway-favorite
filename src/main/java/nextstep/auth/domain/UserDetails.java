package nextstep.auth.domain;

public interface UserDetails {
    boolean checkPassword(String password);
}
