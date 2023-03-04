package nextstep.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자 입니다."),
    NOT_FOUND_FAVORITE(HttpStatus.BAD_REQUEST, "존재하지 않는 즐겨찾기 입니다."),
    CAN_NOT_DELETE_FAVORITE(HttpStatus.BAD_REQUEST, "다른 사용자의 즐겨찾기는 삭제할 수 없습니다."),
    NO_PATH(HttpStatus.BAD_REQUEST, "두 역은 연결된 경로가 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자 입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근할 수 없는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
