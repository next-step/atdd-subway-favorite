package nextstep.subway.domain.exception;

import lombok.Getter;

@Getter
public class SubwayDomainException extends RuntimeException {
    private final SubwayDomainExceptionType exceptionType;
    private final String detail;

    public SubwayDomainException() {
        super(SubwayDomainExceptionType.INTERNAL_SERVER_ERROR.getMessage());
        this.exceptionType = SubwayDomainExceptionType.INTERNAL_SERVER_ERROR;
        this.detail = exceptionType.getMessage();
    }

    public SubwayDomainException(SubwayDomainExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = exceptionType.getMessage();
    }

    public SubwayDomainException(SubwayDomainExceptionType exceptionType, String detail) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = detail;
    }
}
