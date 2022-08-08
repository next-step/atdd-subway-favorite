package nextstep.common.exception.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String code();

    HttpStatus status();
}
