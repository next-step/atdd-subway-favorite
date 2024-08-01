package nextstep.subway.domain.exception;

public class NotFoundLineException extends SubwayDomainException {
    public NotFoundLineException(Long id) {
        super(SubwayDomainExceptionType.NOT_FOUND_LINE, "lineId " + id + " is invalid");
    }
}
