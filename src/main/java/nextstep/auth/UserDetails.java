package nextstep.auth;

@FunctionalInterface
public interface UserDetails {
    boolean checkPassword(String password);
}
