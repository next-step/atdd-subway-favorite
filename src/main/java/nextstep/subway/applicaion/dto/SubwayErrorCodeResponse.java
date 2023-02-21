package nextstep.subway.applicaion.dto;

import nextstep.common.exception.ErrorCode;

public class SubwayErrorCodeResponse {

    private String message;

    private String code;

    private SubwayErrorCodeResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public static SubwayErrorCodeResponse of(ErrorCode errorCode) {
        return new SubwayErrorCodeResponse(errorCode.getMessage(), errorCode.getCode());
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
