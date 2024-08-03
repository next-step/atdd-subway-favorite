package nextstep.configuration.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.base.exception.BaseDomainException;
import nextstep.subway.domain.exception.SubwayDomainException;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    String error;
    String message;
    String detail;

    public static ErrorResponse fromSubwayException(SubwayDomainException e) {
        return new ErrorResponse(e.getExceptionType().getName(), e.getExceptionType().getMessage(), e.getDetail());
    }

    public static ErrorResponse fromBaseException(BaseDomainException e) {
        return new ErrorResponse(e.getName(), e.getMessage(), e.getDetail());
    }
}
