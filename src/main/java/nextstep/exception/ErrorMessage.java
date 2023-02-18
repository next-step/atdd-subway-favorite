package nextstep.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    /**
     * member
     */
    MEMBER_PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다."),
    ;

    ErrorMessage(HttpStatus httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    private final HttpStatus httpStatus;
    private final String description;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }
}
