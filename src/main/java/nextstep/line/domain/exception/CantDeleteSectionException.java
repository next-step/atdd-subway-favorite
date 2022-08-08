package nextstep.line.domain.exception;

public class CantDeleteSectionException extends IllegalSectionOperationException {
    public static final String NOT_ENOUGH_SECTION = "구간이 둘 이상일때만 삭제할 수 있습니다.";

    public CantDeleteSectionException(String message) {
        super(message);
    }
}
