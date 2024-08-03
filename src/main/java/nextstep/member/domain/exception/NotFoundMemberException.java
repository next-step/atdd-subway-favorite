package nextstep.member.domain.exception;

public class NotFoundMemberException extends MemberDomainException {
    public NotFoundMemberException(Long id) {
        super(MemberDomainExceptionType.NOT_FOUND_MEMBER, "member id " + id + " was not found");
    }

    public NotFoundMemberException() {
        super(MemberDomainExceptionType.NOT_FOUND_MEMBER);
    }
}
