package nextstep.common.exception;

public enum ErrorResponse {
    INVALIDATION_LOGIN_INFORMATION("로그인 정보가 틀렸습니다."),
    NOT_FOUND_EMAIL("없는 이메일 입니다."),
    NOT_FOUND_STATION("없는 지하철입니다"),
    NOT_FOUND_FAVORITE("없는 즐겨찾기 정보입니다."),
    INVALID_TOKEN_VALUE("유효하지 않은 토큰 값입니다."),
    FORBIDDEN("접근권한이 없는 요청입니다."),

    ;

    private String message;

    ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
