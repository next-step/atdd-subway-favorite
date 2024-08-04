package nextstep.subway.domain.exception;

import lombok.Getter;
import nextstep.base.exception.BaseDomainException;

@Getter
public class SubwayDomainException extends BaseDomainException {
    private final SubwayDomainExceptionType exceptionType;
    private final String detail;

    public SubwayDomainException() {
        super();
        this.exceptionType = SubwayDomainExceptionType.INTERNAL_SERVER_ERROR;
        this.detail = exceptionType.getMessage();
    }

    public SubwayDomainException(SubwayDomainExceptionType exceptionType) {
        super(exceptionType.getStatus(), exceptionType.getName(), exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = exceptionType.getMessage();
    }

    public SubwayDomainException(SubwayDomainExceptionType exceptionType, String detail) {
        super(exceptionType.getStatus(), exceptionType.getName(), exceptionType.getMessage(), detail);
        this.exceptionType = exceptionType;
        this.detail = detail;
    }
}
