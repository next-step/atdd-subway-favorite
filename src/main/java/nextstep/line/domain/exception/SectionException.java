package nextstep.line.domain.exception;

public class SectionException extends RuntimeException {
    private static String DEFAULT_MESSAGE = "구간 에러 : ";
    public SectionException() {
        super(DEFAULT_MESSAGE);
    }

    public SectionException(String message) {
        super(message);
    }

    public SectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SectionException(Throwable cause) {
        super(cause);
    }

    protected SectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

