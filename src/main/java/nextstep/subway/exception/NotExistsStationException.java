package nextstep.subway.exception;

public class NotExistsStationException extends RuntimeException{
    public NotExistsStationException() {
        super();
    }

    public NotExistsStationException(String message) {
        super(message);
    }

    public NotExistsStationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistsStationException(Throwable cause) {
        super(cause);
    }

    protected NotExistsStationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
