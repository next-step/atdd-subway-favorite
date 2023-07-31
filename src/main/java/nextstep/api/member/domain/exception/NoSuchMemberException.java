package nextstep.api.member.domain.exception;

import nextstep.api.SubwayException;

public class NoSuchMemberException extends SubwayException {
    public NoSuchMemberException(final String message) {
        super(message);
    }

    public static NoSuchMemberException from(final Long id) {
        return new NoSuchMemberException("회원을 찾을 수 없습니다 : id=" + id);
    }

    public static NoSuchMemberException from(final String email) {
        return new NoSuchMemberException("회원을 찾을 수 없습니다 : email=" + email);
    }
}
