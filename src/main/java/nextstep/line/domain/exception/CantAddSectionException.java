package nextstep.line.domain.exception;

public class CantAddSectionException extends IllegalSectionOperationException {
    public static final String ALREADY_INCLUDED_SECTION = "이미 노선에 포함된 구간입니다.";

    public CantAddSectionException(String message) {
        super(message);
    }
}
