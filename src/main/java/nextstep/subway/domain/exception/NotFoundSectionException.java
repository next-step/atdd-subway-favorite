package nextstep.subway.domain.exception;

public class NotFoundSectionException extends SubwayDomainException {
    public NotFoundSectionException(String detail) {
        super(SubwayDomainExceptionType.NOT_FOUND_LINE_SECTION, detail);
    }
}
