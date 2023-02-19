package nextstep.subway.config.message;

public enum SubwayError {
    NOT_FOUND("존재하지 않습니다. 입력한 정보를 확인해주세요."),
    ;

    private final String message;

    SubwayError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
