package nextstep.member.domain;

public interface PasswordCheckableUser {
    boolean checkPassword(String password);
}
