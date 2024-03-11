package nextstep.member.application;

public interface UserDetails {
    String getMemberEmail();
    boolean isEqualPassword(String password);
}
