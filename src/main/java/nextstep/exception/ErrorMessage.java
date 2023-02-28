package nextstep.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    /**
     * member auth
     */
    MEMBER_PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다."),
    TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST, "토큰의 유효기간이 지났습니다."),

    /**
     * member
     */
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 회원이 존재하지 않습니다."),

    RESOURCE_NOT_FOUND(HttpStatus.BAD_REQUEST, "자원을 찾을 수 없습니다.");

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
