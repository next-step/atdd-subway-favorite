package nextstep.argumentResolver;

public enum ErrorCode {
    MISMATCH_BEARER_PREFIX_OF_JWT_TOKEN("Bearer Prefix가 정확하지 않습니다."),
    UNVERIFIED_BEARER_TOKEN("해당 서비스에 사용되는 토큰이 아닙니다."),
    UNREGISTERED_STATION("등록되지 않은 역입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
