package nextstep.base.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseDomainException extends RuntimeException {
    private final int status;
    private final String name;
    private final String message;
    private final String detail;

    public BaseDomainException() {
        super("internal server error");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.name = "INTERNAL_SERVER_ERROR";
        this.message = "internal server error";
        this.detail = "internal server error";
    }

    public BaseDomainException(int status, String name, String message) {
        super(message);
        this.status = status;
        this.name = name;
        this.message = message;
        this.detail = message;
    }

    public BaseDomainException(int status, String name, String message, String detail) {
        super(message);
        this.status = status;
        this.name = name;
        this.message = message;
        this.detail = detail;
    }
}
