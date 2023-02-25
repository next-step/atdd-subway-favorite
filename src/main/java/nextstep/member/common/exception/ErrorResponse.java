package nextstep.member.common.exception;

public enum ErrorResponse {
    INVALIDATION_LOGIN_INFORMATION("로그인 정보가 틀렸습니다."),
    NOT_FOUND_EMAIL("없는 이메일 입니다."),

    ;

    private String message;

    ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
