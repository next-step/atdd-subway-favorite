package nextstep.member.application.exception;

public enum ErrorCode {
    CANNOT_ADD_NOT_EXIST_PATH("조회할 수 없는 경로는 즐겨찾기로 등록할 수 없습니다"),
    CANNOT_DELETE_NOT_EXIST_FAVORITE("존재하지 않는 즐겨찾기 입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
