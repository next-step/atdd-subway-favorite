package nextstep.auth.config.message;

public enum AuthError {
    NOT_MISSING_TOKEN("토큰 정보가 없습니다."),
    NOT_VALID_TOKEN("유효하지 않은 토큰입니다."),
    ;

    private final String message;

    AuthError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
