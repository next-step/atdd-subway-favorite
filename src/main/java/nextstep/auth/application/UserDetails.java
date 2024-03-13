package nextstep.auth.application;

public interface UserDetails {
    String getMemberEmail();
    boolean isEqualPassword(String password);
}
