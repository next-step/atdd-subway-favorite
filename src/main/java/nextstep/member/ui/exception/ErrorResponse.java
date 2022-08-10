package nextstep.member.ui.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private Integer code;
    private String message;

    public static ErrorResponse of(Integer code, String message) {
        return new ErrorResponse(code, message);
    }
}
