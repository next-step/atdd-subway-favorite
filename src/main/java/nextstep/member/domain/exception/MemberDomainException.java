package nextstep.member.domain.exception;

import lombok.Getter;
import nextstep.base.exception.BaseDomainException;

@Getter
public class MemberDomainException extends BaseDomainException {
    private final MemberDomainExceptionType exceptionType;
    private final String detail;

    public MemberDomainException() {
        super();
        this.exceptionType = MemberDomainExceptionType.INTERNAL_SERVER_ERROR;
        this.detail = exceptionType.getMessage();
    }

    public MemberDomainException(MemberDomainExceptionType exceptionType) {
        super(exceptionType.getStatus(), exceptionType.getName(), exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = exceptionType.getMessage();
    }

    public MemberDomainException(MemberDomainExceptionType exceptionType, String detail) {
        super(exceptionType.getStatus(), exceptionType.getName(), exceptionType.getMessage(), detail);
        this.exceptionType = exceptionType;
        this.detail = detail;
    }
}
