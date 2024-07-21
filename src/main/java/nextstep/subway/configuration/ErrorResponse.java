package nextstep.subway.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
}
