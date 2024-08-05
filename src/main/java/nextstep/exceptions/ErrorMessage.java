package nextstep.exceptions;

import java.util.function.Function;

public enum ErrorMessage {

    INSUFFICIENT_STATIONS("구간이 1개밖에 없어 역을 삭제할 수 없습니다."),
    NOT_TERMINUS_STATION("삭제하려는 역이 종착역이 아닙니다."),
    LINE_HAS_NO_STATION("기존 역이 존재하지 않습니다."),
    LINE_NOT_FOUND("존재하지 않는 지하철 노선입니다."),
    INVALID_DOWN_STATION("하행역으로 등록하려는 역이 이미 존재합니다."),
    NON_EXISTENT_STATION("존재하지 않는 역입니다", (id) -> String.format("%s : 존재하지 않는 역입니다.", id)),
    SECTION_NOT_FOUND("존재하지 않는 구간입니다."),
    POSITIVE_DISTANCE("거리는 1이상이어야 합니다."),
    DIFFERENT_STATIONS("상행역과 하행역은 다른 역이어야 합니다."),
    PATH_NOT_FOUND("경로를 찾을 수 없습니다"),


    //즐겨찾기
    FAVORITE_NOT_FOUND("즐겨찾기를 찾을 수 없습니다"),
    FAVORITE_ALREADY_EXISTS("즐겨찾기가 이미 존재합니다"),


    //회원 관련
    UNAUTHORIZED("인증되지 않은 사용자입니다."),
    FORBIDDEN("권한이 없습니다");
    private final String message;
    private final Function<String, String> formatMessage;

    ErrorMessage(final String message) {
        this.message = message;
        this.formatMessage = null;
    }

    ErrorMessage(final String message, final Function<String, String> formatMessage) {
        this.message = message;
        this.formatMessage = formatMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattingMessage(final String id) {
        if (formatMessage == null) {
            return message;
        }
        return formatMessage.apply(id);
    }

    public String getFormattingMessage(final Long id) {
        return getFormattingMessage(id.toString());
    }

}
