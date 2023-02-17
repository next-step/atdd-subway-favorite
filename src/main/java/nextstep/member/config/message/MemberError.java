package nextstep.member.config.message;

public enum MemberError {
    UNAUTHORIZED("인증에 실패하였습니다. 입력한 정보를 확인해주세요."),
    NOT_INPUT_EMAIL("입력한 Email을 확인해주세요."),
    ;

    private final String message;

    MemberError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
