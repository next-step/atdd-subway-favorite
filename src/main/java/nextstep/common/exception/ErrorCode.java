package nextstep.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String code();

    HttpStatus status();
}
