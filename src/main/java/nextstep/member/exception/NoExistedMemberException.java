package nextstep.member.exception;

public class NoExistedMemberException extends RuntimeException{
    public NoExistedMemberException(String email) {
        super("회원을 찾을 수 없습니다. 이메일 : " + email);
    }
}
