package nextstep.auth.domain.exception;

import lombok.Getter;
import nextstep.base.exception.BaseDomainException;

@Getter
public class AuthDomainException extends BaseDomainException {
    private final AuthDomainExceptionType exceptionType;
    private final String detail;

    public AuthDomainException() {
        super();
        this.exceptionType = AuthDomainExceptionType.INTERNAL_SERVER_ERROR;
        this.detail = exceptionType.getMessage();
    }

    public AuthDomainException(AuthDomainExceptionType exceptionType) {
        super(exceptionType.getStatus(), exceptionType.getName(), exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = exceptionType.getMessage();
    }

    public AuthDomainException(AuthDomainExceptionType exceptionType, String detail) {
        super(exceptionType.getStatus(), exceptionType.getName(), exceptionType.getMessage(), detail);
        this.exceptionType = exceptionType;
        this.detail = detail;
    }
}
