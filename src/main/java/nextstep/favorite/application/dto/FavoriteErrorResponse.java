package nextstep.favorite.application.dto;

import nextstep.common.exception.ErrorCode;

public class FavoriteErrorResponse {

    private String message;

    private String code;

    private FavoriteErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public static FavoriteErrorResponse of(ErrorCode errorCode) {
        return new FavoriteErrorResponse(errorCode.getMessage(), errorCode.getCode());
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
